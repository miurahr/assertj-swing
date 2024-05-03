# Troubleshooting GUI Test Failures

Although AssertJ Swing makes it easy to write GUI tests, troubleshooting GUI test failures can be a
challenging task. The following links explain the features that AssertJ Swing provides to help us
figure out why a GUI test failed.

- <a href="assertj-swing-troubleshooting-screenshots.html">Screenshots of Test Failures</a>
- <a href="assertj-swing-troubleshooting-lookups.html">Troubleshooting Component Lookups</a>

## AssertJ Swing tests Failing in a CI server

Users have reported that some AssertJ Swing tests failed when executed in a continuous integration server running on Windows.
> **_Cecil Williams_**
> We are using Team City 4.0.2 with the agent running on a Windows XP machine. If the screen is
> locked (via screen saver or Ctrl-Alt-Del) when the FEST tests run any assertions involving
> "requires message" fail. If we unlock the screen the tests pass. Has anyone else encountered this
> and do you have a solution other than keeping the screen unlocked (security risk).

**Possible solutions** can be found in <a href="http://groups.google.com/group/easytesting/browse_frm/thread/fa0a9077b0b7dc9c" target="_blank">this mail thread</a>.
