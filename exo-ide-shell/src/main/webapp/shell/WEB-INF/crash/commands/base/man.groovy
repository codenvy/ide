/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
import org.crsh.command.DescriptionMode
import org.crsh.command.CRaSHCommand
import org.crsh.cmdline.annotations.Usage
import org.crsh.cmdline.annotations.Command
import org.crsh.cmdline.annotations.Argument;

class man extends CRaSHCommand {
  @Usage("format and display the on-line manual pages")
  @Command
  Object main(@Usage("the command") @Argument String command) throws ScriptException {
    def cmd = shell.getCommand(command);
    if (cmd != null) {
      return  cmd.describe(unmatched, DescriptionMode.MAN);
    } else {
      return "Command $command not found";
    }
  }
}