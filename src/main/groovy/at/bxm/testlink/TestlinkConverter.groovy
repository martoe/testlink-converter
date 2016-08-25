package at.bxm.testlink

import groovy.xml.MarkupBuilder
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory

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
    xml.root {
      for (int i = 0; i < 10; i++) {
        blah(x: i)
      }
    }
    return writer as String
  }
}
