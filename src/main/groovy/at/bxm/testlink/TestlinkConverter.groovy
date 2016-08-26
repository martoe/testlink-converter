package at.bxm.testlink

import groovy.util.logging.Slf4j
import groovy.xml.MarkupBuilder
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory

import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

import static java.lang.Thread.currentThread
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI

/**
 * Documentation: https://github.com/TestLinkOpenSourceTRMS/testlink-documentation/tree/master/end-users/1.9
 * A Excel-XML converter: https://github.com/MrBricodage/TestLink--ImportExcelMacro
 *
 */
@Slf4j
class TestlinkConverter {

  def idCounter
  def xml

  String asTestcases(File excelFile) {
    return parse(excelFile, false)
  }

  String asTestsuite(File excelFile) {
    return parse(excelFile, true)
  }

  private String parse(File excelFile, boolean asTestsuite) {
    idCounter = 0
    def xmlWriter = new StringWriter()
    xml = new MarkupBuilder(xmlWriter)
    if (!excelFile.exists() || !excelFile.canRead() || !excelFile.file) {
      throw new FileNotFoundException("Cannot read local file $excelFile.absolutePath")
    }
    def wb = WorkbookFactory.create(excelFile)
    if (wb.numberOfSheets > 1 && wb.activeSheetIndex != 0) {
      throw new InvalidFormatException("File contains multiple sheets - which one to use?")
    }
    if (asTestsuite) {
      readTestsuite(wb.getSheetAt(0))
    } else {
      readTestcases(wb.getSheetAt(0))
    }
    return xmlWriter.toString()
  }

  private void readTestcases(Sheet sheet) {
    xml.testcases {
      def testCaseRows = []
      sheet.rowIterator().each { row ->
        String testId = row.getCell(0)
        if (testId != "TF_ID") { // skip header line
          if (testId) { // start of a new testcase block
            readTestcase(testCaseRows)
            testCaseRows.clear()
          }
          String testGoal = row.getCell(1)
          String testAction = row.getCell(4)
          if (testGoal || testAction) {
            testCaseRows.add(row)
          } else {
            log.info("Skipping empty row " + (row.rowNum + 1))
          }
        }
      }
      readTestcase(testCaseRows)
    }
  }

  private void readTestsuite(Sheet sheet) {
    xml.testsuite(id: 1, name: sheet.sheetName) {
      node_order(1)
      details(sheet.sheetName)
      def testCaseRows = []
      sheet.rowIterator().each { row ->
        String testId = row.getCell(0)
        if (testId != "TF_ID") { // skip header line
          if (testId) { // start of a new testcase block
            readTestcase(testCaseRows)
            testCaseRows.clear()
          }
          String testGoal = row.getCell(1)
          String testAction = row.getCell(4)
          if (testGoal || testAction) {
            testCaseRows.add(row)
          } else {
            log.info("Skipping empty row " + (row.rowNum + 1))
          }
        }
      }
      readTestcase(testCaseRows)
    }
  }

  private void readTestcase(List<Row> rows) {
    if (rows) {
      String testId = rows[0].getCell(0)
      String testGoal = rows[0].getCell(1)
      String testPrecond = rows[0].getCell(2)
      // ...
      xml.testcase(internalid: idCounter++, name: testId) {
        node_order(1000)
        version(1)
        summary(testGoal)
        preconditions(testPrecond)
        execution_type(1)
        importance(2)
        estimated_exec_duration {}
        status(1)
        is_open(1)
        active(1)
        steps {
          rows.eachWithIndex { row, testStepNo ->
            String testStepAction = row.getCell(4)
            String testStepDesc = row.getCell(5)
            String testStepResult = row.getCell(6)
            step {
              step_number(testStepNo + 1)
              actions("$testStepAction <br/> $testStepDesc")
              expectedresults(testStepResult)
              execution_type(1)
            }
          }
        }
      }
    }
  }

  static void validate(String xml) {
    URL schema = currentThread().getContextClassLoader().getResource("testcases.xsd");
    SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI)
      .newSchema(schema)
      .newValidator()
      .validate(new StreamSource(new StringReader(xml)))
  }
}
