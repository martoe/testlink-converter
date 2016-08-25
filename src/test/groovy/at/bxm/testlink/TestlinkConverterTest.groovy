package at.bxm.testlink

import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class TestlinkConverterTest {

  @Test
  void "convert simple file"() {
    def converted = new TestlinkConverter().convert("src/test/resources/simple-testcase.xlsx" as File)
    assertThat(converted, equalTo("""<testcases>
  <testcase internalid='0' name='F-001'>
    <node_order>1000</node_order>
    <version>1</version>
    <summary>Test Case Summary</summary>
    <preconditions>Preconditions</preconditions>
    <execution_type>1</execution_type>
    <importance>2</importance>
    <estimated_exec_duration />
    <status>1</status>
    <is_open>1</is_open>
    <active>1</active>
    <steps>
      <step>
        <step_number>1</step_number>
        <actions>1. Do first</actions>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>2</step_number>
        <actions>2. Do next</actions>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>3</step_number>
        <actions>3. Do last</actions>
        <expectedresults>Expected result</expectedresults>
        <execution_type>1</execution_type>
      </step>
    </steps>
  </testcase>
</testcases>"""))
  }

  @Test
  void "convert real data"() {
    def file = "Regressionsliste_FICO_Kommentare Christoph_überarbeitet"
    new File("target/${file}.xml").text = new TestlinkConverter().convert(new File("src/test/resources/${file}.xlsx"))
  }
}
