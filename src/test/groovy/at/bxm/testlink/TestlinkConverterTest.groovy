package at.bxm.testlink

import org.junit.Test

class TestlinkConverterTest {

  @Test
  void test() {
    println new TestlinkConverter().convert("src/test/resources/empty.xlsx" as File)
  }
}
