from pygments.lexer import RegexLexer
from pygments.token import Keyword, Name, Operator, Text

# This code came from ChatGPT, so consider it a placeholder.

class AugustaLexer(RegexLexer):
    name = 'Augusta'
    aliases = ['augusta']
    filenames = ['*.ag']

    tokens = {
        'root': [
            (r'\b(if|else|while|for|return)\b', Keyword),
            (r'[a-zA-Z_][a-zA-Z0-9_]*', Name),
            (r'[=+\-*/]', Operator),
            (r'\s+', Text),
        ]
    }
