
README
======

This folder contains the main Augusta documentation set. It is only a placeholder right now.

The Augusta documentation is written using reStructuredText (reST) and compiled with Sphinx into
presentation formats. To set up Sphinx, first install a suitable version of Python (3.6 or
later) and then create a virtual environment in the `doc` folder as follows:

```bash
python -m venv venv
```

This runs the module `venv` with the argument `venv`, which creates a virtual environment in the
`venv` folder. To activate the virtual environment, run the following command:

```bash
source venv/bin/activate
```

The same commands work on Windows, but the `source` command is replaced by `.\venv\Scripts\activate`.

When the virtual environment is activated for the first time, do:
    
```bash 
pip install -r requirements.txt
```

This installs the required packages based on the list in `requirements.txt`. You can then build
the documentation by changing into the desired subfolder and running the following command:

```bash
make html
```

See the README files in the subfolders for more information about the contents of those
subfolders.

