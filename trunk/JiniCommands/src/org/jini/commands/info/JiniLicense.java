/*
 * Licensed under GNU GENERAL PUBLIC LICENSE Version 1 you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/gpl-1.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * For a copy of the License type 'license'
 */
package org.jini.commands.info;

import java.text.SimpleDateFormat;
import java.util.Date;
import jinicommands.JiniCmd;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
/**
 * 
 * @author Norman David <normandavid67@gmail.com>
 * @since JiniCommands version 0.1
 * @version 1.0
 */

public class JiniLicense extends JiniCmd {

    CommandLineParser jcParser = new BasicParser();
    Options jcOptions = new Options();
    private boolean done;
    static String jiniVersion = "0.1";

    @Override
    public void setJCLIOptions() {
    }

    @Override
    public void executeCommand() {
    }
    
      public static String getJiniCommandsVersion(){
        return jiniVersion;
    }

    public JiniLicense() {

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");

        System.out.println("");
        System.out.println("GNU GENERAL PUBLIC LICENSE");
        System.out.println("Version , "+JiniLicense.getJiniCommandsVersion()+"  "+ ft.format(date));
        System.out.println("");
        System.out.println("Copyright (C) " + yy.format(date) + " Norman David Switzerland.");
        System.out.println("");
        System.out.println("Everyone is permitted to copy and distribute verbatim copies");
        System.out.println("of this license document, but changing it is not allowed.");
        System.out.println("");
        System.out.println("Preamble");
        System.out.println("");
        System.out.println("The license agreements of most software companies try to keep users");
        System.out.println("at the mercy of those companies.  By contrast, our General Public");
        System.out.println("License is intended to guarantee your freedom to share and change free");
        System.out.println("software--to make sure the software is free for all its users.  The");
        System.out.println("General Public License applies to the Free Software Foundation's");
        System.out.println("software and to any other program whose authors commit to using it.");
        System.out.println("You can use it for your programs, too.");
        System.out.println("");
        System.out.println("When we speak of free software, we are referring to freedom, not");
        System.out.println("price.  Specifically, the General Public License is designed to make");
        System.out.println("sure that you have the freedom to give away or sell copies of free");
        System.out.println("software, that you receive source code or can get it if you want it,");
        System.out.println("that you can change the software or use pieces of it in new free");
        System.out.println("programs; and that you know you can do these things.");
        System.out.println("");
        System.out.println("To protect your rights, we need to make restrictions that forbid");
        System.out.println("anyone to deny you these rights or to ask you to surrender the rights.");
        System.out.println("These restrictions translate to certain responsibilities for you if you");
        System.out.println("distribute copies of the software, or if you modify it.");
        System.out.println("");
        System.out.println("For example, if you distribute copies of a such a program, whether");
        System.out.println("gratis or for a fee, you must give the recipients all the rights that");
        System.out.println("you have.  You must make sure that they, too, receive or can get the");
        System.out.println("source code.  And you must tell them their rights.");
        System.out.println("");
        System.out.println("We protect your rights with two steps: (1) copyright the software, and");
        System.out.println("(2) offer you this license which gives you legal permission to copy,");
        System.out.println("distribute and/or modify the software.");
        System.out.println("");
        System.out.println("Also, for each author's protection and ours, we want to make certain");
        System.out.println("that everyone understands that there is no warranty for this free");
        System.out.println("software.  If the software is modified by someone else and passed on, we");
        System.out.println("want its recipients to know that what they have is not the original, so");
        System.out.println("that any problems introduced by others will not reflect on the original");
        System.out.println("authors' reputations.");
        System.out.println("");
        System.out.println("The precise terms and conditions for copying, distribution and");
        System.out.println("modification follow.");
        System.out.println("");
        System.out.println("GNU GENERAL PUBLIC LICENSE");
        System.out.println("TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION");
        System.out.println("");
        System.out.println("0. This License Agreement applies to any program or other work which");
        System.out.println("contains a notice placed by the copyright holder saying it may be");
        System.out.println("distributed under the terms of this General Public License.  The");
        System.out.println("\"Program\", below, refers to any such program or work, and a \"work based\"");
        System.out.println("on the Program\" means either the Program or any work containing the");
        System.out.println("Program or a portion of it, either verbatim or with modifications.  Each");
        System.out.println("licensee is addressed as \"you\".");
        System.out.println("");
        System.out.println("1. You may copy and distribute verbatim copies of the Program's source");
        System.out.println("code as you receive it, in any medium, provided that you conspicuously and");
        System.out.println("appropriately publish on each copy an appropriate copyright notice and");
        System.out.println("disclaimer of warranty; keep intact all the notices that refer to this");
        System.out.println("General Public License and to the absence of any warranty; and give any");
        System.out.println("other recipients of the Program a copy of this General Public License");
        System.out.println("along with the Program.  You may charge a fee for the physical act of");
        System.out.println("transferring a copy.");
        System.out.println("");
        System.out.println("2. You may modify your copy or copies of the Program or any portion of");
        System.out.println("it, and copy and distribute such modifications under the terms of Paragraph");
        System.out.println("1 above, provided that you also do the following:");
        System.out.println("");
        System.out.println("a) cause the modified files to carry prominent notices stating that");
        System.out.println("you changed the files and the date of any change; and");
        System.out.println("");
        System.out.println("b) cause the whole of any work that you distribute or publish, that");
        System.out.println("in whole or in part contains the Program or any part thereof, either");
        System.out.println("with or without modifications, to be licensed at no charge to all");
        System.out.println("third parties under the terms of this General Public License (except");
        System.out.println("that you may choose to grant warranty protection to some or all");
        System.out.println("third parties, at your option).");
        System.out.println("");
        System.out.println("c) If the modified program normally reads commands interactively when");
        System.out.println("run, you must cause it, when started running for such interactive use");
        System.out.println("in the simplest and most usual way, to print or display an");
        System.out.println("announcement including an appropriate copyright notice and a notice");
        System.out.println("that there is no warranty (or else, saying that you provide a");
        System.out.println("warranty) and that users may redistribute the program under these");
        System.out.println("conditions, and telling the user how to view a copy of this General");
        System.out.println("Public License.");
        System.out.println("");
        System.out.println("d) You may charge a fee for the physical act of transferring a");
        System.out.println("copy, and you may at your option offer warranty protection in");
        System.out.println("exchange for a fee.");
        System.out.println("");
        System.out.println("Mere aggregation of another independent work with the Program (or its");
        System.out.println("derivative) on a volume of a storage or distribution medium does not bring");
        System.out.println("the other work under the scope of these terms.");
        System.out.println("");
        System.out.println("3. You may copy and distribute the Program (or a portion or derivative of");
        System.out.println("it, under Paragraph 2) in object code or executable form under the terms of");
        System.out.println("Paragraphs 1 and 2 above provided that you also do one of the following:");
        System.out.println("");
        System.out.println("a) accompany it with the complete corresponding machine-readable");
        System.out.println("source code, which must be distributed under the terms of");
        System.out.println("Paragraphs 1 and 2 above; or,");
        System.out.println("");
        System.out.println("b) accompany it with a written offer, valid for at least three");
        System.out.println("years, to give any third party free (except for a nominal charge");
        System.out.println("for the cost of distribution) a complete machine-readable copy of the");
        System.out.println("corresponding source code, to be distributed under the terms of");
        System.out.println("Paragraphs 1 and 2 above; or,");
        System.out.println("");
        System.out.println("c) accompany it with the information you received as to where the");
        System.out.println("corresponding source code may be obtained.  (This alternative is");
        System.out.println("allowed only for noncommercial distribution and only if you");
        System.out.println("received the program in object code or executable form alone.)");
        System.out.println("");
        System.out.println("Source code for a work means the preferred form of the work for making");
        System.out.println("modifications to it.  For an executable file, complete source code means");
        System.out.println("all the source code for all modules it contains; but, as a special");
        System.out.println("exception, it need not include source code for modules which are standard");
        System.out.println("libraries that accompany the operating system on which the executable");
        System.out.println("file runs, or for standard header files or definitions files that");
        System.out.println("accompany that operating system.");
        System.out.println("");
        System.out.println("4. You may not copy, modify, sublicense, distribute or transfer the");
        System.out.println("Program except as expressly provided under this General Public License.");
        System.out.println("Any attempt otherwise to copy, modify, sublicense, distribute or transfer");
        System.out.println("the Program is void, and will automatically terminate your rights to use");
        System.out.println("the Program under this License.  However, parties who have received");
        System.out.println("copies, or rights to use copies, from you under this General Public");
        System.out.println("License will not have their licenses terminated so long as such parties");
        System.out.println("remain in full compliance.");
        System.out.println("");
        System.out.println("5. By copying, distributing or modifying the Program (or any work based");
        System.out.println("on the Program) you indicate your acceptance of this license to do so,");
        System.out.println("and all its terms and conditions.");
        System.out.println("");
        System.out.println("6. Each time you redistribute the Program (or any work based on the");
        System.out.println("Program), the recipient automatically receives a license from the original");
        System.out.println("licensor to copy, distribute or modify the Program subject to these");
        System.out.println("terms and conditions.  You may not impose any further restrictions on the");
        System.out.println("recipients' exercise of the rights granted herein.");
        System.out.println("");
        System.out.println("7. The Free Software Foundation may publish revised and/or new versions");
        System.out.println("of the General Public License from time to time.  Such new versions will");
        System.out.println("be similar in spirit to the present version, but may differ in detail to");
        System.out.println("address new problems or concerns.");
        System.out.println("");
        System.out.println("Each version is given a distinguishing version number.  If the Program");
        System.out.println("specifies a version number of the license which applies to it and \"any");
        System.out.println("later version\", you have the option of following the terms and conditions");
        System.out.println("either of that version or of any later version published by the Free");
        System.out.println("Software Foundation.  If the Program does not specify a version number of");
        System.out.println("the license, you may choose any version ever published by the Free Software");
        System.out.println("Foundation.");
        System.out.println("");
        System.out.println("8. If you wish to incorporate parts of the Program into other free");
        System.out.println("programs whose distribution conditions are different, write to the author");
        System.out.println("to ask for permission.  For software which is copyrighted by the Free");
        System.out.println("Software Foundation, write to the Free Software Foundation; we sometimes");
        System.out.println("make exceptions for this.  Our decision will be guided by the two goals");
        System.out.println("of preserving the free status of all derivatives of our free software and");
        System.out.println("of promoting the sharing and reuse of software generally.");
        System.out.println("");
        System.out.println("NO WARRANTY");
        System.out.println("");
        System.out.println("9. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY");
        System.out.println("FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW.  EXCEPT WHEN");
        System.out.println("OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES");
        System.out.println("PROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED");
        System.out.println("OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF");
        System.out.println("MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS");
        System.out.println("TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE");
        System.out.println("PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,");
        System.out.println("REPAIR OR CORRECTION.");
        System.out.println("");
        System.out.println("10. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING");
        System.out.println("WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR");
        System.out.println("REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES,");
        System.out.println("INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING");
        System.out.println("OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED");
        System.out.println("TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY");
        System.out.println("YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER");
        System.out.println("PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE");
        System.out.println("POSSIBILITY OF SUCH DAMAGES.");
        System.out.println("");
        System.out.println("END OF TERMS AND CONDITIONS");
        System.out.println("");
        System.out.println("Appendix: How to Apply These Terms to Your New Programs");
        System.out.println("");
        System.out.println("If you develop a new program, and you want it to be of the greatest");
        System.out.println("possible use to humanity, the best way to achieve this is to make it");
        System.out.println("free software which everyone can redistribute and change under these");
        System.out.println("terms.");
        System.out.println("");
        System.out.println("To do so, attach the following notices to the program.  It is safest to");
        System.out.println("attach them to the start of each source file to most effectively convey");
        System.out.println("the exclusion of warranty; and each file should have at least the");
        System.out.println("\"copyright\" line and a pointer to where the full notice is found.");
        System.out.println("");
        System.out.println("<one line to give the program's name and a brief idea of what it does.>");
        System.out.println("Copyright (C) 19yy  <name of author>");
        System.out.println("");
        System.out.println("This program is free software; you can redistribute it and/or modify");
        System.out.println("it under the terms of the GNU General Public License as published by");
        System.out.println("the Free Software Foundation; either version 1, or (at your option)");
        System.out.println("any later version.");
        System.out.println("");
        System.out.println("This program is distributed in the hope that it will be useful,");
        System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
        System.out.println("GNU General Public License for more details.");
        System.out.println("");
        System.out.println("You should have received a copy of the GNU General Public License");
        System.out.println("along with this program; if not, write to the Free Software");
        System.out.println("Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA  02110-1301 USA");
        System.out.println("------------------------------------------------------------------------------");
            
    }
}
