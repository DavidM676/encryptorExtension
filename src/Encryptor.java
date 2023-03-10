public class Encryptor {
    /** A two-dimensional array of single-character strings, instantiated in the constructor */
    private String[][] letterBlock;

    /** The number of rows of letterBlock, set by the constructor */
    private int numRows;

    /** The number of columns of letterBlock, set by the constructor */
    private int numCols;

    /** Constructor*/
    public Encryptor(int r, int c) {
        letterBlock = new String[r][c];
        numRows = r;
        numCols = c;
        characterShift = cS;
    }

    public String[][] getLetterBlock() {
        return letterBlock;
    }

    public static String shiftString(String input, int shift) {
        String output = "";
        int length = input.length();
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c)) {
                int shifted = ((int) c)+shift;
                output += ((char) shifted);
            } else {
                output += c;
            }
        }
        return output.toString();
    }

    public  static String[][] shiftRow(String[][] input, int shift) {
        if (shift>=input.length) {
            shift = input.length%shift;
        }
    }
    public void fillBlock(String str) {
        int currentIdx = 0;
        for (int i = 0; i<numRows; i++) {
            for (int j = 0; j<numCols; j++) {
                if (currentIdx<str.length()) {
                    letterBlock[i][j] = str.substring(currentIdx, currentIdx + 1);
                } else {
                    letterBlock[i][j] = "A";
                }
                currentIdx++;
            }
        }
    }



    public String encryptBlock() {
        String str = "";
        for (int i = 0; i<numCols; i++) {
            for (int j = 0; j<numRows; j++) {
                str+=letterBlock[j][i];
            }
        }
        return str;
    }

    public String superEncryptMessage(String message, int characterShift) {
        message = shiftString(message, characterShift);
        return encryptMessage(message);
    }

    public String superDecryptMessage(String message, int characterShift) {
        message = shiftString(message, characterShift);
        return decryptMessage(message);
    }

    public String encryptMessage(String message) {
        /* to be implemented in part (c) */

        int chunk = numCols*numRows;
        int end = chunk;
        int start = 0;
        String str = "";
        while (message.length()>=end-1) {
            String part = message.substring(start, end);
            start = end;
            end+=chunk;
            fillBlock(part);
            str+=encryptBlock();
        }

        if ((message.length()%chunk) != 0) {
            String part = message.substring(start, message.length());
            fillBlock(part);
            str += encryptBlock();
        }

        return str;
    }



    public String decryptMessage(String encryptedMessage) {
        String[] block = new String[(encryptedMessage.length()/numRows)+1];

        int x = 0;

        for (int i = 0; i<encryptedMessage.length(); i++) {
//            if ((i%numRows)!=0) {
//                block[x] += encryptedMessage.substring(i, i + 1);
//                x++;
//            }
            if (block[x]!=null) {
                block[x] += encryptedMessage.substring(i, i + 1);
            } else {
                block[x] = encryptedMessage.substring(i, i+1);
            }

            if (((i+1)%numRows)==0) {
                x++;
            }
//            block[x] = encryptedMessage.substring(i, i+1);
        }


        int count=0;
        String wrd = "";

        String[] smallBlock = new String[numCols];
        for (int z = 0; z<block.length; z++) {
//            System.out.print(block[i]+"|");
            smallBlock[count] = block[z];
            count ++;
            if ((z+1)%numCols==0) {
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        wrd += (smallBlock[j].substring(i, i + 1));
                    }
                }

                count = 0;
            }

        }


        while (wrd.endsWith("A")) {
            wrd=wrd.substring(0, wrd.length()-1);
        }

        return wrd;
    }



}