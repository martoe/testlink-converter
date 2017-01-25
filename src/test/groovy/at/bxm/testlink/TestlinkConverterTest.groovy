package at.bxm.testlink

import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class TestlinkConverterTest {

  @Test
  void "convert simple file to testcases"() {
    def converted = new TestlinkConverter().asTestcases("src/test/resources/simple-testcase.xlsx" as File)
    assertThat(converted, equalTo("""<testcases>
  <testcase internalid='0' name='T-001'>
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
        <actions>OL Login &lt;br/&gt; Do first</actions>
        <expectedresults></expectedresults>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>2</step_number>
        <actions>Menü Suche &lt;br/&gt; Do next</actions>
        <expectedresults></expectedresults>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>3</step_number>
        <actions>Postkorbeintrag auswählen &lt;br/&gt; Do last</actions>
        <expectedresults>Expected result</expectedresults>
        <execution_type>1</execution_type>
      </step>
    </steps>
  </testcase>
</testcases>"""))
  }

  @Test
  void "convert simple file to testsuite"() {
    def converted = new TestlinkConverter().asTestsuite("src/test/resources/simple-testcase.xlsx" as File)
    assertThat(converted, equalTo("""<testsuite id='1' name='Regression_Name_Test'>
  <node_order>1</node_order>
  <details>Regression_Name_Test</details>
  <testcase internalid='0' name='T-001'>
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
        <actions>OL Login &lt;br/&gt; Do first</actions>
        <expectedresults></expectedresults>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>2</step_number>
        <actions>Menü Suche &lt;br/&gt; Do next</actions>
        <expectedresults></expectedresults>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>3</step_number>
        <actions>Postkorbeintrag auswählen &lt;br/&gt; Do last</actions>
        <expectedresults>Expected result</expectedresults>
        <execution_type>1</execution_type>
      </step>
    </steps>
  </testcase>
</testsuite>"""))
  }

  @Test
  void "convert real data to testcases"() {
    def file = "Regressionsliste_neu_Template"
    new File("target/${file}.xml").text = new TestlinkConverter().asTestcases(new File("src/test/resources/${file}.xlsx"))
  }

  @Test
  void "convert real data to testsuite"() {
    def file = "Regressionsliste_neu_Template"
    new File("target/$file-suite.xml").text = new TestlinkConverter().asTestsuite(new File("src/test/resources/${file}.xlsx"))
  }

  @Test
  void "convert real data to multiple testcase files"() {
    def file = "Regressionsliste_neu_Template"
    def data = new TestlinkConverter().allSheetsAsTestcases(new File("src/test/resources/${file}.xlsx"))
    data.each { name, xml ->
      new File("target/$file-${name}.xml").text = xml
    }
  }
}
