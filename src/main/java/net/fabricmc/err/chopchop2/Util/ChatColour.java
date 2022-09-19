package net.fabricmc.err.chopchop2.Util;

public enum ChatColour {
    BLACK('0', 0x00),
    DARK_BLUE('1', 0x1),
    DARK_GREEN('2', 0x2),
    DARK_AQUA('3', 0x3),
    DARK_RED('4', 0x4),
    DARK_PURPLE('5', 0x5),
    GOLD('6', 0x6),
    GRAY('7', 0x7),
    DARK_GRAY('8', 0x8),
    BLUE('9', 0x9),
    GREEN('a', 0xA),
    AQUA('b', 0xB),
    RED('c', 0xC),
    LIGHT_PURPLE('d', 0xD),
    YELLOW('e', 0xE),
    WHITE('f', 0xF),
    MAGIC('k', 0x10),
    BOLD('l', 0x11),
    STRIKETHROUGH('m', 0x12),
    UNDERLINE('n', 0x13),
    ITALIC('o', 0x14),
    RESET('r', 0x15);

    public static final char COLOR_CHAR = '\u00A7';
	
    private final String data;
    
    private ChatColour(char code, int intCode) {
        this.data = new String(new char[] {COLOR_CHAR, code});
    }

    @Override
    public String toString() {
        return data;
    }
}
