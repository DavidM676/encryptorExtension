
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


    public static String[][] shiftRow(String[][] array, int shift) {
        String[][] result = new String[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            int newIndex = (i + shift) % array.length;
            result[newIndex] = array[i];
        }

        return result;
    }

    public static String[][] shiftColumn(String[][] array, int shift) {
        String[][] result = new String[array.length][array[0].length];
        for (int i = 0; i<array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[i][((j + shift) % array[i].length)] = array[i][j];
            }
        }
        return result;
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

    public String unFillBlock(String[][] array) {
        String wrd = "";
        for (int i = 0; i<array.length; i++) {
            for (int j = 0; j< array[i].length; j++) {
                wrd += array[i][j];
            }
        }
        return wrd;
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

    public static String removeA(String message) {
        while (message.endsWith("A")) {
            message=message.substring(0, message.length()-1);
        }
        return message;
    }

    public String superEncryptMessage(String message, int characterShift, int rowShift, int colShift) {
        String finalMessage = "";

        message = shiftString(message, characterShift);


        int chunk = numCols*numRows;
        int end = chunk;
        int start = 0;
        String str = "";
        while (message.length()>=end-1) {
            String part = message.substring(start, end);
            start = end;
            end+=chunk;
            fillBlock(part);

            String[][] rs = shiftRow(letterBlock, rowShift);
            String[][] cs = shiftColumn(rs, colShift);
            finalMessage +=  unFillBlock(cs);


        }

        if ((message.length()%chunk) != 0) {
            String part = message.substring(start, message.length());
            fillBlock(part);

            String[][] rs = shiftRow(letterBlock, rowShift);
            String[][] cs = shiftColumn(rs, colShift);
            finalMessage +=  unFillBlock(cs);


        }

//        message = shiftString(message, characterShift);
//        fillBlock(message);
//        String[][] rs = shiftRow(letterBlock, rowShift);
//        String[][] cs = shiftColumn(rs, colShift);
//        message = unFillBlock(cs);
//
        return encryptMessage(finalMessage);
    }

    public String superDecryptMessage(String message, int characterShift, int rowShift, int colShift) {
        String finalMessage = "";
        message =  decryptMessage(message);


        int chunk = numCols*numRows;
        int end = chunk;
        int start = 0;
        String str = "";
        while (message.length()>=end-1) {
            String part = message.substring(start, end);
            start = end;
            end+=chunk;
            fillBlock(part);

            int reverseShiftC = letterBlock[0].length - (colShift % letterBlock[0].length);
            String[][] uncs = shiftColumn(letterBlock, reverseShiftC);
            int reverseShiftR = uncs.length - (rowShift % uncs.length);
            String[][] unrs = shiftRow(uncs, reverseShiftR);
            finalMessage += unFillBlock(unrs);



        }

        if ((message.length()%chunk) != 0) {
            String part = message.substring(start, message.length());
            fillBlock(part);

            int reverseShiftC = letterBlock[0].length - (colShift % letterBlock[0].length);
            String[][] uncs = shiftColumn(letterBlock, reverseShiftC);
            int reverseShiftR = uncs.length - (rowShift % uncs.length);
            String[][] unrs = shiftRow(uncs, reverseShiftR);
            finalMessage += unFillBlock(unrs);


        }
        finalMessage = removeA(finalMessage);
        finalMessage = shiftString(finalMessage, -characterShift);
        return finalMessage;

//        message =  decryptMessage(message);
//        fillBlock(message); //re adds A
//        int reverseShiftC = letterBlock[0].length - (colShift % letterBlock[0].length);
//        String[][] uncs = shiftColumn(letterBlock, reverseShiftC);
//        int reverseShiftR = uncs.length - (rowShift % uncs.length);
//        String[][] unrs = shiftRow(uncs, reverseShiftR);
//        message = unFillBlock(unrs);
//
//        message = removeA(message);
//
//        message = shiftString(message, -characterShift);
//        return message;

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

            if (block[x]!=null) {
                block[x] += encryptedMessage.substring(i, i + 1);
            } else {
                block[x] = encryptedMessage.substring(i, i+1);
            }

            if (((i+1)%numRows)==0) {
                x++;
            }
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

        wrd = removeA(wrd);

        return wrd;
    }



}