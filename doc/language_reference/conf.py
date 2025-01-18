# Configuration file for the Sphinx documentation builder.
#
# For the full list of built-in configuration values, see the documentation:
# https://www.sphinx-doc.org/en/master/usage/configuration.html

# This sets up the Augusta lexer for syntax highlighting in the documentation.
import sys
import os

sys.path.insert(0, os.path.abspath('../sphinx-extensions'))
from augusta_lexer import AugustaLexer

def setup(app):
    app.add_lexer('augusta', AugustaLexer)

# -- Project information -----------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#project-information

project = 'Augusta Language Reference'
copyright = '2025, Augusta Contributors'
author = 'Augusta Contributors'

# -- General configuration ---------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#general-configuration

extensions = []

templates_path = ['_templates']
exclude_patterns = ['_build', 'Thumbs.db', '.DS_Store']



# -- Options for HTML output -------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#options-for-html-output

html_theme = 'alabaster'
html_static_path = ['_static']
