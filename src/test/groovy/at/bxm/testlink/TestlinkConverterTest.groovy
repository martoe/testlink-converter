package at.bxm.testlink

import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class TestlinkConverterTest {

  @Test
  void test() {
    def converted = new TestlinkConverter().convert("src/test/resources/empty.xlsx" as File)
    assertThat(converted, equalTo("""<testcases>
  <testcase internalid='6' name='Test Case'>
    <node_order>1000</node_order>
    <version>1</version>
    <summary>Test Case Summary</summary>
    <preconditions />
    <execution_type>1</execution_type>
    <importance>2</importance>
    <estimated_exec_duration />
    <status>1</status>
    <is_open>1</is_open>
    <active>1</active>
    <steps>
      <step>
        <step_number>1</step_number>
        <actions>&lt;p&gt; step 1 &lt;/p&gt;</actions>
        <expectedresults>&lt;p&gt; result 1 &lt;/p&gt;</expectedresults>
        <execution_type>1</execution_type>
      </step>
      <step>
        <step_number>2</step_number>
        <actions>&lt;p&gt; step 2 &lt;/p&gt;</actions>
        <expectedresults>&lt;p&gt; result 2 &lt;/p&gt;</expectedresults>
        <execution_type>1</execution_type>
      </step>
    </steps>
  </testcase>
</testcases>"""))
  }
}
