package at.bxm.testlink

import groovy.xml.MarkupBuilder
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory

import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

import static java.lang.Thread.currentThread
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI

/**
 * Documentation: https://github.com/TestLinkOpenSourceTRMS/testlink-documentation/tree/master/end-users/1.9
 * A Ecxel-XML converter: https://github.com/MrBricodage/TestLink--ImportExcelMacro
 *
 */
class TestlinkConverter {

  String convert(File excelFile) {
    if (!excelFile.exists() || !excelFile.canRead() || !excelFile.file) {
      throw new FileNotFoundException("Cannot read local file $excelFile.absolutePath")
    }
    def wb = WorkbookFactory.create(excelFile)
    if (wb.numberOfSheets > 1 && wb.activeSheetIndex != 0) {
      throw new InvalidFormatException("File contains multiple sheets - which one to use?")
    }
    return processSheet(wb.getSheetAt(0))
  }

  String processSheet(Sheet sheet) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def counter = 0
    xml.testcases {

      sheet.rowIterator().each { row ->
        String testId = row.getCell(0)
        if (testId && testId != "TF_ID") { // skip header lines
          String testGoal = row.getCell(1)
          String testPrecond = row.getCell(2)
          def testSteps = (row.getCell(3) as String).readLines()
          String testExpectation = row.getCell(4)
          String testData = row.getCell(5)
          //...

          testcase(internalid: counter++, name: testId) {
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
              testSteps.eachWithIndex { String testStep, int testStepNo ->
                step {
                  step_number(testStepNo + 1)
                  actions(testStep)
                  if (testStepNo == testSteps.size() - 1) {
                    expectedresults(testExpectation)
                  }
                  execution_type(1)
                }
              }
            }
          }
        }
      }
    }
    return writer as String
  }

  static void validate(String xml) {
    URL schema = currentThread().getContextClassLoader().getResource("testcases.xsd");
    SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI)
      .newSchema(schema)
      .newValidator()
      .validate(new StreamSource(new StringReader(xml)))
  }
}
