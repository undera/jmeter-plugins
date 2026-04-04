# Developers Guidelines

## Code & Commits
Primary repository is located here: <https://github.com/undera/jmeter-plugins>

Every change must be noted in [Changelog](/wiki/Changelog/)'s "Next Release" section immediately after commit
to track progress and mention changes in [Changelog](/wiki/Changelog/) on release. See also [BuildingFromSource](/wiki/BuildingFromSource/)

Code must not contain @author tags, use [Contributors](/wiki/Contributors/) page to mark your contribution instead

## Unit Tests
Committed code must be covered with unit tests. This practice helps us to maintain some quality level and avoid stupid bugs like NPEs in GUI.
The easiest way to see the unit tests coverage is to run `mvn cobertura:cobertura install` command, and look into *target/site/cobertura* directories for HTML report.

See also: [codecov.io report](https://codecov.io/gh/undera/jmeter-plugins)

## GUI & Usability
There is strong tradition for the project to be convinient and obvious for users. That's come from our slogan. Every GUI should be designed carefully.

Every plugin having GUI must have hyperlink to wiki and version mention.

Please check also the [PluginsGuiGuidelines](/wiki/PluginsGuiGuidelines/)

## Documentation
Project wiki pages are contained in the GitHub repository <https://github.com/undera/jmeter-plugins/tree/master/site/dat/wiki>. Documentation updates should be committed with actual code changes.

Wiki pages should have screenshots demonstrating component GUI, images are stored in GitHub: <https://github.com/undera/jmeter-plugins/tree/master/site/img/wiki>

There may not be undocumented features. This means every plugin must have its documentation page and links to that page from wiki Start page and corresponding plugin set page.


# Example Test Plans
It is good practice to provide an example test plan for complex and not too obvious components. Usually it is a JMX Test Plan file with instructions in comment fields. The example should be self-containing as much as possible, using Dummy Samplers to have predictable results. Take a look at existing example test plans to have idea how it should look like.

Example test plans are committed into GitHub repository here: <https://github.com/undera/jmeter-plugins/tree/master/examples>

# Release Process
  1. Edit pom.xml and set version name to version releasing
  1. edit downloads page: move prev release to latest, add new release
  1. edit changes list in [Changelog](/wiki/Changelog/) into released state
  1. edit homepage: latest version info, download links, don't forget links from separate sets
  1. edit wiki Start page: plugins list with NEW tags
  1. do mvn clean package
  1. upload plugins zip and libs zip to downloads
  1. commit and push release doc changes
  1. go to https://github.com/undera/jmeter-plugins/releases and create release
  1. announce release:
    1. project mailing list
    1. twitter link to mailing list announce
    1. linkedIn jmeter group
    1. software-testing.ru forum (for Russian users)
    1. jmeter mailing list (in case of important enough release)
  1. publish maven artifacts maybe after a while to prevent buggy artifacts
    1. run maven-bundle.sh
    1. upload at <https://oss.sonatype.org/index.html#staging-upload> - login 'undera'
    1. stage at <https://oss.sonatype.org/index.html#stagingRepositories>
    
