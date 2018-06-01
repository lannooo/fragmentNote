# fragmentNote
## Info
using ***JNativeHook*** to watching global keyboard events. When I'm looking some
text and want to save to my note, JUST **CTRL+C** to get them into clipboard, 
then **CTRL+SHIFT+V** to call this tool to save.
The tool can be run at background when you close the initial window, and **CTRL+SHIFT+V** will 
call it out again.
## Build
Intellij -> Project Structure -> Artifacts -> Add Jars from modules with dependencies
choose "copy to the output directory..." and set the MANIFEST.MF directory to src/main/resources
At output root, make a new directory "lib" and add dependency jars into it.
Click Ok to save and Build. The output file will be a mynote.jar and a lib directory.
## Run
Just override the mynote.jar in the "run" directory.
Modify conf/mynote.properties
- directory.md={the directory you want to store markdown file}
- record.types={types of your note, join with a "," e.g. MySQL,Oracle,SqlServer}
Double click run.bat to run it.
## Using
after run, just close the init window, it will run at background.
copy some words and type **CTRL+SHIFT+V** to test. Enter title ,Choose Type, Save.
At System tray, it appears as a ugly icon, right click to exit tool, or add a new Note Type.
## Updates
- 2017-11-3   first version

support save note to markdown,
a note record contains title, type, content, keywords(generated from content or title, not implement yet)
