## About FragmentNote
This is a tool for quick note-taking. The typical scenario goes like this: 
Discover some interesting text -> **CTRL+C** to get them into the clipboard -> **CTRL+SHIFT+V** to call this tool to save quickly.
This tool is mainly based on the ***JNativeHook*** for global keyboard events listening and supports running in the background.

## key features
- support save records to markdown, a record contains title, type, content

## Build
- IntelliJ -> Project Structure -> Artifacts -> Add Jars from modules with dependencies;
- Choose "copy to the output directory..." and set the MANIFEST.MF directory to src/main/resources;
- At the output root, make a new directory "lib" and add dependency jars into it;
- Click OK to save and Build. The output file will be a *mynote.jar* and a lib directory.

## Run
- Override the mynote.jar in the "run" directory.
- Modify conf/mynote.properties
  - directory.md={the directory you want to store the markdown file}
  - record.types={types of your note, join with a "," e.g. MySQL, Oracle, SqlServer}
- Double-click run.bat to run it.

## Updates
- 2017/11/3   first updates
- Not maintained anymore
