# Datasource File Fixed Width

Pentaho Reporting plugin that allows you to extract data from a fixed-width file.

# Installation

Copy the compiled `jar` to the `lib` directory under your Pentaho Reporting instance.

# Usage

Given the following fixed-width file, let's see what this would look like if we imported this into Pentaho Reporting via this plugin.

```
020171011 Ben     L Toyot
S    ABC   YOU  0
S    123   GET  0
S    ZYX   THE  0
S    TYU   IDEA 0
```

You'll first need to create the new fixed-file datasource under: Data -> Data Sets -> Advanced -> File Fixed Width Data Access

![datasource](/images/datasource.png)

When opened, you will be presented with a dialog similar to the following (the row identifiers and fields have already been entered for this example).

![dialog](/images/dialog.png)

You will then see the fields you've defined and have access to the data in your fixed-width file!

![tree](/images/tree.png)
