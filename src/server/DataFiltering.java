package server;

public class DataFiltering {
    public static String[] sliceStr(String txt, int index) {
        try {
            String[] blocks = txt.split("]\\[");
            if (index >= blocks.length) return new String[0];

            String targetBlock = blocks[index].replace("[", "").replace("]", "");
            if (targetBlock.startsWith(",")) {
                targetBlock = targetBlock.substring(1);
            }
            return targetBlock.split(",");

        } catch (Exception e) {
            System.err.println("Erro ao fatiar String: " + e.getMessage());
            return new String[0];
        }
    }


    public static boolean formatType(String txt, String type){
        txt = txt.trim();
        boolean correctType = false;
        switch (type) {
            case "int":
                try{
                    Integer.parseInt(txt);
                    return  true;
                }catch (Exception e){
                    System.out.println("NOT A INTEGER");
                }
                break;
            case "float":
                try {
                    Float.parseFloat(txt);
                    return  true;
                }catch (Exception e){
                    System.out.println("NOT A FLOAT");
                }
                break;
            case "boolean":
                    if(txt.equals("t") || txt.equals("1") || txt.equals("true")) correctType = true;
                break;
            case "char":
                    try{
                        txt.charAt(0);
                        return true;
                    }catch (Exception e){
                        System.out.println("NOT A CHARACTER");
                    }
            case "double":
                try {
                    Double.parseDouble(txt);
                    return  true;
                }catch (Exception e){
                    System.out.println("NOT A DOUBLE");
                }
                break;
            case "long":
                try {
                    Long.parseLong(txt);
                    return  true;
                }catch (Exception e){
                    System.out.println("NOT A LONG");
                }
                break;
        }
        return  correctType;
    }


    public static int contVar(String data){
        String[] dataList = sliceStr(data,0);
        return dataList.length;
    }

    public static String prepareStatement(int indexSquare) {
        String statement = "";
        if(formatType(Var.squares.get(indexSquare).sendValue, Var.squares.get(indexSquare).type)){
            statement = "[" + Var.squares.get(indexSquare).pointer + "][" + Var.squares.get(indexSquare).sendValue + "][" + Var.squares.get(indexSquare).type + "]";
        }else {
            Var.squares.get(indexSquare).sendValue = "Invalid type!!";
        }
        return statement;
    }
}
