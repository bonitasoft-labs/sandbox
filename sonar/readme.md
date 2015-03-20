**Sonar client project**

Client project that extracts metrics values from Sonar server and store them in csv files. 1 file per project monitored by sonar.<br>
The list of projects is specified as input of the extractor.<br>

***Note***:you can override properties defined in sonar.properties file in not-on-vcs.properties (this file will not be stored in git) with sensible values (i.e. username and password).


List of metrics currently extracted:<br>
 "authors_by_line"",<br>
 "test_failures"",<br>
 "new_overall_coverage"",<br>
 "overall_lines_to_cover"",<br>
 "new_overall_lines_to_cover"",<br>
 "overall_uncovered_lines"",<br>
 "lines"",<br>
 "package_tangles"",<br>
 "package_feedback_edges"",<br>
 "file_tangles"",<br>
 "package_edges_weight"",<br>
 "file_cycles"",<br>
 "file_tangle_index"",<br>
 "file_feedback_edges"",<br>
 "overall_conditions_by_line"",<br>
 "overall_covered_conditions_by_line"",<br>
 "duplicated_blocks"",<br>
 "ca"",<br>
 "ce"",<br>
 "dsm"",<br>
 "package_cycles"",<br>
 "build_longest_duration"",<br>
 "build_shortest_duration"",<br>
 "build_average_time_to_fix_failure"",<br>
 "build_longest_time_to_fix_failure"",<br>
 "build_average_builds_to_fix_failure"",<br>
 "build_durations"",<br>
 "build_results"",<br>
 "useless-duplicated-lines"",<br>
 "total-useless-lines"",<br>
 "dead-code"",<br>
 "potential-dead-code"",<br>
 "it_conditions_by_line"",<br>
 "function_complexity_distribution"",<br>
 "overall_uncovered_conditions"",<br>
 "new_overall_uncovered_conditions"",<br>
 "overall_branch_coverage"",<br>
 "new_overall_branch_coverage"",<br>
 "duplicated_files"",<br>
 "security-violations"",<br>
 "weighted-security-violations"",<br>
 "security-rci"",<br>
 "security-violations-distribution"",<br>
 "duplicated_lines_density"",<br>
 "duplications_data"",<br>
 "weighted_violations"",<br>
 "builds"",<br>
 "build_failures"",<br>
 "build_success_density"",<br>
 "build_average_duration"",<br>
 "violations_density"",<br>
 "blocker_violations"",<br>
 "critical_violations"",<br>
 "test_errors"",<br>
 "skipped_tests"",<br>
 "test_data"",<br>
 "comment_lines"",<br>
 "comment_lines_density"",<br>
 "comment_blank_lines"",<br>
 "class_complexity"",<br>
 "complexity_in_functions"",<br>
 "function_complexity"",<br>
 "class_complexity_distribution"",<br>
 "it_coverage"",<br>
 "burned_budget"",<br>
 "business_value"",<br>
 "team_size"",<br>
 "new_it_coverage"",<br>
 "it_lines_to_cover"",<br>
 "new_it_lines_to_cover"",<br>
 "it_uncovered_lines"",<br>
 "new_it_uncovered_lines"",<br>
 "it_line_coverage"",<br>
 "new_it_line_coverage"",<br>
 "it_coverage_line_hits_data"",<br>
 "package_tangle_index"",<br>
 "it_conditions_to_cover"",<br>
 "new_it_conditions_to_cover"",<br>
 "it_uncovered_conditions"",<br>
 "new_it_uncovered_conditions"",<br>
 "it_branch_coverage"",<br>
 "new_it_branch_coverage"",<br>
 "duplicated_lines"",<br>
 "temp-method-lines"",<br>
 "file_complexity"",<br>
 "complexity_in_classes"",<br>
 "issues"",<br>
 "test_success_density"",<br>
 "coverage"",<br>
 "new_coverage"",<br>
 "lines_to_cover"",<br>
 "new_lines_to_cover"",<br>
 "uncovered_lines"",<br>
 "new_uncovered_lines"",<br>
 "coverage_line_hits_data"",<br>
 "line_coverage"",<br>
 "new_line_coverage"",<br>
 "conditions_to_cover"",<br>
 "new_conditions_to_cover"",<br>
 "violations"",<br>
 "it_covered_conditions_by_line"",<br>
 "overall_coverage"",<br>
 "minor_violations"",<br>
 "info_violations"",<br>
 "new_violations"",<br>
 "new_blocker_violations"",<br>
 "new_critical_violations"",<br>
 "new_major_violations"",<br>
 "new_minor_violations"",<br>
 "uncovered_conditions"",<br>
 "new_uncovered_conditions"",<br>
 "branch_coverage"",<br>
 "new_branch_coverage"",<br>
 "branch_coverage_hits_data"",<br>
 "conditions_by_line"",<br>
 "covered_conditions_by_line"",<br>
 "generated_lines"",<br>
 "ncloc"",<br>
 "generated_ncloc"",<br>
 "classes"",<br>
 "files"",<br>
 "directories"",<br>
 "packages"",<br>
 "functions"",<br>
 "accessors"",<br>
 "statements"",<br>
 "public_api"",<br>
 "projects"",<br>
 "file_complexity_distribution"",<br>
 "tests"",<br>
 "test_execution_time"",<br>
 "major_violations"",<br>
 "public_documented_api_density"",<br>
 "new_overall_uncovered_lines"",<br>
 "overall_line_coverage"",<br>
 "new_overall_line_coverage"",<br>
 "overall_coverage_line_hits_data"",<br>
 "overall_conditions_to_cover"",<br>
 "new_overall_conditions_to_cover"",<br>
 "new_info_violations"",<br>
 "false_positive_issues"",<br>
 "open_issues"",<br>
 "reopened_issues"",<br>
 "confirmed_issues"",<br>
 "abstractness"",<br>
 "instability"",<br>
 "distance"",<br>
 "dit"",<br>
 "noc"",<br>
 "rfc"",<br>
 "rfc_distribution"",<br>
 "lcom4"",<br>
 "lcom4_blocks"",<br>
 "lcom4_distribution"",<br>
 "suspect_lcom4_density"",<br>
 "public_undocumented_api"",<br>
 "commented_out_code_lines"",<br>
 "complexity"",<br>
 "file_edges_weight"",<br>
 "revisions_by_line"",<br>
 "last_commit_datetimes_by_line"",<br>
 "unreviewed_violations"",<br>
 "new_unreviewed_violations"",<br>
 "false_positive_reviews"",<br>
 "active_reviews"",<br>
 "unassigned_reviews"",<br>
 "unplanned_reviews"",<br>
 "sqale_index"",<br>
 "new_technical_debt"",<br>
 "ncloc_data"",<br>
 "comment_lines_data"",<br>
 "alert_status"",<br>
 "profile"",<br>
 "profile_version"

Information:
<hr>
Whole list of metrics available on the Sonar server with their key,	name and description (as returned by the Sonar REST API)

<pre>
[
{
	"key":"test_failures",
	"name":"Unit tests failures",
	"description":"Number of unit test failures",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_overall_coverage",
	"name":"Overall coverage on new code",
	"description":"Overall coverage of new/changed code",
	"domain":"Tests (Overall)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"overall_lines_to_cover",
	"name":"Overall lines to cover",
	"description":"Lines to cover by all tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":true},
{
	"key":"new_overall_lines_to_cover",
	"name":"Overall lines to cover on new code",
	"description":"New lines to cover by all tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"overall_uncovered_lines",
	"name":"Overall uncovered lines",
	"description":"Uncovered lines by all tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"lines",
	"name":"Lines",
	"description":"Lines",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"package_tangles",
	"name":"File dependencies to cut",
	"description":"File dependencies to cut",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"package_feedback_edges",
	"name":"Package dependencies to cut",
	"description":"Package dependencies to cut",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"file_tangles",
	"name":"File tangles",
	"description":"Files tangles",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"package_edges_weight",
	"name":"Package edges weight",
	"description":"Package edges weight",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":true},
{
	"key":"file_cycles",
	"name":"File cycles",
	"description":"File cycles",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"file_tangle_index",
	"name":"File tangle index",
	"description":"File tangle index",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"PERCENT",
	"hidden":true},
{
	"key":"file_feedback_edges",
	"name":"Suspect file dependencies",
	"description":"Suspect file dependencies",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"overall_conditions_by_line",
	"name":"Overall branches by line",
	"description":"Overall branches by all tests and by line",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"authors_by_line",
	"name":"Authors by line",
	"description":"Authors by line",
	"domain":"SCM",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"overall_covered_conditions_by_line",
	"name":"Overall covered branches by line",
	"description":"Overall covered branches by all tests and by line",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"duplicated_blocks",
	"name":"Duplicated blocks",
	"description":"Duplicated blocks",
	"domain":"Duplication",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"ca",
	"name":"Afferent couplings",
	"description":"Afferent couplings",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"ce",
	"name":"Efferent couplings",
	"description":"Efferent couplings",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"dsm",
	"name":"Dependency Matrix",
	"description":"Dependency Matrix",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"package_cycles",
	"name":"Package cycles",
	"description":"Package cycles",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"build_longest_duration",
	"name":"Longest duration",
	"description":"Duration of longest successful build",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"MILLISEC",
	"hidden":false},
{
	"key":"build_shortest_duration",
	"name":"Shortest duration",
	"description":"Duration of shortest successful build",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"MILLISEC",
	"hidden":false},
{
	"key":"build_average_time_to_fix_failure",
	"name":"Average time to fix a failure",
	"description":"Average time to fix a failure",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"MILLISEC",
	"hidden":false},
{
	"key":"build_longest_time_to_fix_failure",
	"name":"Longest time to fix a failure",
	"description":"Longest time to fix a failure",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"MILLISEC",
	"hidden":false},
{
	"key":"build_average_builds_to_fix_failure",
	"name":"Average number of builds between fixes",
	"description":"Average number of builds between fixes",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"build_durations",
	"name":"Durations",
	"description":"Durations",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"build_results",
	"name":"Results",
	"description":"Results",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"useless-duplicated-lines",
	"name":"Useless Duplicated Lines",
	"description":"Number of duplicated lines that could be reduced\"",
	"domain":"Duplication",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"total-useless-lines",
	"name":"Total Useless Code",
	"description":"Number of lines that can be reduced",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"dead-code",
	"name":"Dead Code",
	"description":"Code that is not used and could be removed",
	"domain":"Rules",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"potential-dead-code",
	"name":"Potential Dead Code",
	"description":"Code that is potentially not used and could be removed",
	"domain":"Rules",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"it_conditions_by_line",
	"name":"IT branches by line",
	"description":"IT branches by line",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"function_complexity_distribution",
	"name":"Functions distribution /complexity",
	"description":"Functions distribution /complexity",
	"domain":"Complexity",
	"qualitative":true,"user_managed":false,"direction":0,"val_type":"DISTRIB",
	"hidden":false},
{
	"key":"overall_uncovered_conditions",
	"name":"Overall uncovered branches",
	"description":"Uncovered branches by all tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_overall_uncovered_conditions",
	"name":"Overall uncovered branches on new code",
	"description":"New branches that are not covered by any test",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"overall_branch_coverage",
	"name":"Overall branch coverage",
	"description":"Branch coverage by all tests",
	"domain":"Tests (Overall)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_overall_branch_coverage",
	"name":"Overall branch coverage on new code",
	"description":"Branch coverage of new/changed code by all tests",
	"domain":"Tests (Overall)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"duplicated_files",
	"name":"Duplicated files",
	"description":"Duplicated files",
	"domain":"Duplication",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"security-violations",
	"name":"Security violations",
	"description":"Security violations",
	"domain":"Rules",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"weighted-security-violations",
	"name":"Weighted Security Violations",
	"description":"Weighted Security Violations",
	"domain":"Rules",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"security-rci",
	"name":"Security rules compliance",
	"description":"Security rules compliance",
	"domain":"Rules",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"security-violations-distribution",
	"name":"Security Violations Distribution",
	"description":"Security Violations Distribution",
	"domain":"Rules",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"duplicated_lines_density",
	"name":"Duplicated lines (%)",
	"description":"Duplicated lines balanced by statements",
	"domain":"Duplication",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"duplications_data",
	"name":"Duplications details",
	"description":"Duplications details",
	"domain":"Duplication",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"weighted_violations",
	"name":"Weighted issues",
	"description":"Weighted issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"builds",
	"name":"Builds",
	"description":"Number of builds",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":false},
{
	"key":"build_failures",
	"name":"Failed Builds",
	"description":"Number of failed builds",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"build_success_density",
	"name":"Success Rate (%)",
	"description":"Ratio of successful builds",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"build_average_duration",
	"name":"Average Duration",
	"description":"Average Duration",
	"domain":"Continuous integration",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"MILLISEC",
	"hidden":false},
{
	"key":"violations_density",
	"name":"Rules compliance",
	"description":"Rules compliance",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"blocker_violations",
	"name":"Blocker issues",
	"description":"Blocker issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"critical_violations",
	"name":"Critical issues",
	"description":"Critical issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"test_errors",
	"name":"Unit tests errors",
	"description":"Number of unit test errors",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"skipped_tests",
	"name":"Skipped unit tests",
	"description":"Number of skipped unit tests",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"test_data",
	"name":"Unit tests details",
	"description":"Unit tests details",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"DATA",
	"hidden":false},
{
	"key":"comment_lines",
	"name":"Comment lines",
	"description":"Number of comment lines",
	"domain":"Documentation",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":false},
{
	"key":"comment_lines_density",
	"name":"Comments (%)",
	"description":"Comments balanced by ncloc + comment lines",
	"domain":"Documentation",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"comment_blank_lines",
	"name":"Blank comments",
	"description":"Comments that do not contain comments",
	"domain":"Documentation",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"class_complexity",
	"name":"Complexity /class",
	"description":"Complexity average by class",
	"domain":"Complexity",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"complexity_in_functions",
	"name":"Complexity in functions",
	"description":"Cyclomatic complexity in functions",
	"domain":"Complexity",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"function_complexity",
	"name":"Complexity /function",
	"description":"Complexity average by function",
	"domain":"Complexity",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"class_complexity_distribution",
	"name":"Classes distribution /complexity",
	"description":"Classes distribution /complexity",
	"domain":"Complexity",
	"qualitative":true,"user_managed":false,"direction":0,"val_type":"DISTRIB",
	"hidden":false},
{
	"key":"it_coverage",
	"name":"IT coverage",
	"description":"Integration tests coverage",
	"domain":"Tests (Integration)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"burned_budget",
	"name":"Burned budget",
	"description":"",
	"domain":"Management",
	"qualitative":false,"user_managed":true,"direction":0,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"business_value",
	"name":"Business value",
	"description":"",
	"domain":"Management",
	"qualitative":true,"user_managed":true,"direction":1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"team_size",
	"name":"Team size",
	"description":"",
	"domain":"Management",
	"qualitative":false,"user_managed":true,"direction":0,"val_type":"INT",
	"hidden":false},
{
	"key":"new_it_coverage",
	"name":"Coverage by IT on new code",
	"description":"Integration tests coverage of new/changed code",
	"domain":"Tests (Integration)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"it_lines_to_cover",
	"name":"IT lines to cover",
	"description":"Lines to cover by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":true},
{
	"key":"new_it_lines_to_cover",
	"name":"Lines to cover by IT on new code",
	"description":"Lines to cover on new code by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"it_uncovered_lines",
	"name":"IT uncovered lines",
	"description":"Uncovered lines by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_it_uncovered_lines",
	"name":"Uncovered lines by IT on new code",
	"description":"New lines that are not covered by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"it_line_coverage",
	"name":"IT line coverage",
	"description":"Line coverage by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_it_line_coverage",
	"name":"Line coverage by IT on new code",
	"description":"Integration tests line coverage of added/changed code",
	"domain":"Tests (Integration)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"it_coverage_line_hits_data",
	"name":"IT coverage hits by line",
	"description":"Coverage hits by line by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"package_tangle_index",
	"name":"Package tangle index",
	"description":"Package tangle index",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"it_conditions_to_cover",
	"name":"IT branches to cover",
	"description":"Branches to cover by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":true},
{
	"key":"new_it_conditions_to_cover",
	"name":"Branches to cover by IT on new code",
	"description":"New branches to cover by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true},
{
	"key":"it_uncovered_conditions",
	"name":"IT uncovered branches",
	"description":"Uncovered branches by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_it_uncovered_conditions",
	"name":"Uncovered branches by IT on new code",
	"description":"New branches that are not covered by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"it_branch_coverage",
	"name":"IT branch coverage",
	"description":"Branch coverage by integration tests",
	"domain":"Tests (Integration)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_it_branch_coverage",
	"name":"Branch coverage by IT on new code",
	"description":"Integration tests branch coverage of new/changed code",
	"domain":"Tests (Integration)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"duplicated_lines",
	"name":"Duplicated lines",
	"description":"Duplicated lines",
	"domain":"Duplication",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"temp-method-lines",
	"name":"Temp info on method lines",
	"description":"",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"file_complexity",
	"name":"Complexity /file",
	"description":"Complexity average by file",
	"domain":"Complexity",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"complexity_in_classes",
	"name":"Complexity in classes",
	"description":"Cyclomatic complexity in classes",
	"domain":"Complexity",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"issues",
	"name":"JIRA Issues",
	"description":"Number of JIRA Issues",
	"domain":"Issues",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":false},
{
	"key":"test_success_density",
	"name":"Unit tests success (%)",
	"description":"Density of successful unit tests",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"coverage",
	"name":"Coverage",
	"description":"Coverage by unit tests",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_coverage",
	"name":"Coverage on new code",
	"description":"Coverage of new/changed code",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"lines_to_cover",
	"name":"Lines to cover",
	"description":"Lines to cover",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_lines_to_cover",
	"name":"Lines to cover on new code",
	"description":"Lines to cover on new code",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"uncovered_lines",
	"name":"Uncovered lines",
	"description":"Uncovered lines",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_uncovered_lines",
	"name":"Uncovered lines on new code",
	"description":"Uncovered lines on new code",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"coverage_line_hits_data",
	"name":"Coverage hits by line",
	"description":"Coverage hits by line",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"line_coverage",
	"name":"Line coverage",
	"description":"Line coverage",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_line_coverage",
	"name":"Line coverage on new code",
	"description":"Line coverage of added/changed code",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"conditions_to_cover",
	"name":"Branches to cover",
	"description":"Branches to cover",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true},
{
	"key":"new_conditions_to_cover",
	"name":"Branches to cover on new code",
	"description":"Branches to cover on new code",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true},
{
	"key":"violations",
	"name":"Issues",
	"description":"Issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"it_covered_conditions_by_line",
	"name":"IT covered branches by line",
	"description":"IT covered branches by line",
	"domain":"Tests (Integration)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"overall_coverage",
	"name":"Overall coverage",
	"description":"Overall test coverage",
	"domain":"Tests (Overall)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"minor_violations",
	"name":"Minor issues",
	"description":"Minor issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"info_violations",
	"name":"Info issues",
	"description":"Info issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_violations",
	"name":"New issues",
	"description":"New issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_blocker_violations",
	"name":"New Blocker issues",
	"description":"New Blocker issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_critical_violations",
	"name":"New Critical issues",
	"description":"New Critical issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_major_violations",
	"name":"New Major issues",
	"description":"New Major issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_minor_violations",
	"name":"New Minor issues",
	"description":"New Minor issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"uncovered_conditions",
	"name":"Uncovered branches",
	"description":"Uncovered branches",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"new_uncovered_conditions",
	"name":"Uncovered branches on new code",
	"description":"Uncovered branches on new code",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"branch_coverage",
	"name":"Branch coverage",
	"description":"Branch coverage",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_branch_coverage",
	"name":"Branch coverage on new code",
	"description":"Branch coverage of new/changed code",
	"domain":"Tests",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"branch_coverage_hits_data",
	"name":"Branch coverage hits",
	"description":"Branch coverage hits",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"conditions_by_line",
	"name":"Branches by line",
	"description":"Branches by line",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"covered_conditions_by_line",
	"name":"Covered branches by line",
	"description":"Covered branches by line",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"generated_lines",
	"name":"Generated Lines",
	"description":"Number of generated lines",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"ncloc",
	"name":"Lines of code",
	"description":"Non Commenting Lines of Code",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"generated_ncloc",
	"name":"Generated lines of code",
	"description":"Generated non Commenting Lines of Code",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"classes",
	"name":"Classes",
	"description":"Classes",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"files",
	"name":"Files",
	"description":"Number of files",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"directories",
	"name":"Directories",
	"description":"Directories",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"packages",
	"name":"Packages",
	"description":"Packages",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"functions",
	"name":"Functions",
	"description":"Functions",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"accessors",
	"name":"Accessors",
	"description":"Accessors",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"statements",
	"name":"Statements",
	"description":"Number of statements",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"public_api",
	"name":"Public API",
	"description":"Public API",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"projects",
	"name":"Projects",
	"description":"Number of projects",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"file_complexity_distribution",
	"name":"Files distribution /complexity",
	"description":"Files distribution /complexity",
	"domain":"Complexity",
	"qualitative":true,"user_managed":false,"direction":0,"val_type":"DISTRIB",
	"hidden":false},
{
	"key":"tests",
	"name":"Unit tests",
	"description":"Number of unit tests",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"test_execution_time",
	"name":"Unit tests duration",
	"description":"Execution duration of unit tests",
	"domain":"Tests",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"MILLISEC",
	"hidden":false},
{
	"key":"major_violations",
	"name":"Major issues",
	"description":"Major issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"public_documented_api_density",
	"name":"Public documented API (%)",
	"description":"Public documented classes and functions balanced by ncloc",
	"domain":"Documentation",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_overall_uncovered_lines",
	"name":"Overall uncovered lines on new code",
	"description":"New lines that are not covered by any tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"overall_line_coverage",
	"name":"Overall line coverage",
	"description":"Line coverage by all tests",
	"domain":"Tests (Overall)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"new_overall_line_coverage",
	"name":"Overall line coverage on new code",
	"description":"Line coverage of added/changed code by all tests",
	"domain":"Tests (Overall)",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"overall_coverage_line_hits_data",
	"name":"Overall coverage hits by line",
	"description":"Coverage hits by all tests and by line",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"overall_conditions_to_cover",
	"name":"Overall branches to cover",
	"description":"Branches to cover by all tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":true},
{
	"key":"new_overall_conditions_to_cover",
	"name":"Overall branches to cover on new code",
	"description":"New branches to cover by all tests",
	"domain":"Tests (Overall)",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true},
{
	"key":"new_info_violations",
	"name":"New Info issues",
	"description":"New Info issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"false_positive_issues",
	"name":"False positive issues",
	"description":"False positive issues",
	"domain":"Issues",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"open_issues",
	"name":"Open issues",
	"description":"Open issues",
	"domain":"Issues",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"reopened_issues",
	"name":"Reopened issues",
	"description":"Reopened issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"confirmed_issues",
	"name":"Confirmed issues",
	"description":"Confirmed issues",
	"domain":"Issues",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"abstractness",
	"name":"Abstractness",
	"description":"Abstractness",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"PERCENT",
	"hidden":true},
{
	"key":"instability",
	"name":"Instability",
	"description":"Instability",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"PERCENT",
	"hidden":true},
{
	"key":"distance",
	"name":"Distance",
	"description":"Distance",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"FLOAT",
	"hidden":true},
{
	"key":"dit",
	"name":"Depth in Tree",
	"description":"Depth in Inheritance Tree",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true},
{
	"key":"noc",
	"name":"Number of Children",
	"description":"Number of Children",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true},
{
	"key":"rfc",
	"name":"Response for Class",
	"description":"Response for Class",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"rfc_distribution",
	"name":"Class distribution /RFC",
	"description":"Class distribution /RFC",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":0,"val_type":"DISTRIB",
	"hidden":false},
{
	"key":"lcom4",
	"name":"LCOM4",
	"description":"Lack of Cohesion of Functions",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"lcom4_blocks",
	"name":"LCOM4 blocks",
	"description":"LCOM4 blocks",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":true},
{
	"key":"lcom4_distribution",
	"name":"Class distribution /LCOM4",
	"description":"Class distribution /LCOM4",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":0,"val_type":"DISTRIB",
	"hidden":false},
{
	"key":"suspect_lcom4_density",
	"name":"Suspect LCOM4 density",
	"description":"Density of classes having LCOM4>1",
	"domain":"Design",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"PERCENT",
	"hidden":false},
{
	"key":"public_undocumented_api",
	"name":"Public undocumented API",
	"description":"Public undocumented classes, functions and variables",
	"domain":"Documentation",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"commented_out_code_lines",
	"name":"Commented-out LOC",
	"description":"Commented lines of code",
	"domain":"Documentation",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"complexity",
	"name":"Complexity",
	"description":"Cyclomatic complexity",
	"domain":"Complexity",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":false},
{
	"key":"file_edges_weight",
	"name":"File edges weight",
	"description":"File edges weight",
	"domain":"Design",
	"qualitative":false,"user_managed":false,"direction":1,"val_type":"INT",
	"hidden":true},
{
	"key":"revisions_by_line",
	"name":"Revisions by line",
	"description":"Revisions by line",
	"domain":"SCM",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"last_commit_datetimes_by_line",
	"name":"Last commit dates by line",
	"description":"Last commit dates by line",
	"domain":"SCM",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"unreviewed_violations",
	"name":"Unreviewed violations",
	"description":"Violations that have not been reviewed yet",
	"domain":"Reviews",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"new_unreviewed_violations",
	"name":"New unreviewed violations",
	"description":"New violations that have not been reviewed yet",
	"domain":"Reviews",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"false_positive_reviews",
	"name":"False-positive reviews",
	"description":"Active false-positive reviews",
	"domain":"Reviews",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"active_reviews",
	"name":"Active reviews",
	"description":"Active open and reopened reviews",
	"domain":"Reviews",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"unassigned_reviews",
	"name":"Unassigned reviews",
	"description":"Active unassigned reviews",
	"domain":"Reviews",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"unplanned_reviews",
	"name":"Unplanned reviews",
	"description":"Active unplanned reviews",
	"domain":"Reviews",
	"qualitative":false,"user_managed":false,"direction":-1,"val_type":"INT",
	"hidden":true},
{
	"key":"sqale_index",
	"name":"Technical Debt",
	"description":"Total effort (in days) to fix all the issues on the component and therefore to comply to all the requirements.",
	"domain":"Technical Debt",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"new_technical_debt",
	"name":"Technical Debt on new code",
	"description":"Technical Debt on new code",
	"domain":"Technical Debt",
	"qualitative":true,"user_managed":false,"direction":-1,"val_type":"FLOAT",
	"hidden":false},
{
	"key":"ncloc_data",
	"name":"ncloc_data",
	"description":"",
	"domain":"Size",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":true},
{
	"key":"comment_lines_data",
	"name":"comment_lines_data",
	"description":"",
	"domain":"Documentation",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":true},
{
	""key":"alert_status",
	"name":"Alert",
	"description":"Alert",
	"domain":"General",
	"qualitative":true,"user_managed":false,"direction":1,"val_type":"LEVEL",
	"hidden":false},
{
	"key":"profile",
	"name":"Profile",
	"description":"Selected quality profile",
	"domain":"General",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"DATA",
	"hidden":false},
{
	"key":"profile_version",
	"name":"Profile version",
	"description":"Selected quality profile version",
	"domain":"General",
	"qualitative":false,"user_managed":false,"direction":0,"val_type":"INT",
	"hidden":true}]
</pre>