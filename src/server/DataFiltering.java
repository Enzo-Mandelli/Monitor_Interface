package server;

public class DataFiltering {
    public static String[] sliceStr(String txt, int index){
        int cont = 0;
        String list = "";
        String[] error = new String[5];

        // === clean the String ===
        txt = txt.replace("[", "");
        txt = txt.trim();


        try {
            for (int i = 0; i < txt.length(); i++) {
                if(cont > index) break; // if the loop past the wanted index it breaks the loop
                if (txt.charAt(i) == (']')){ // if a ']' is found that field has ended
                    cont++;
                    i++;
                }
                if(index == cont){
                    txt = txt.replaceFirst(",", ""); // for some reason i was getting 2 colmas so here i just remove the first one
                    for(int j = i; j < txt.length(); j++){
                        if(txt.charAt(j) == ']')break; // if the loop gets to the end of the wanted field it ends
                        list = list + txt.charAt(j); // recreate the list but only with the desired data
                    }
                    return list.split(","); // transform data in an data array
                }
            } //return the list in specific index
        } catch (Exception e) {

        }
        return error = txt.split(",");
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
            System.out.println(Var.squares.get(indexSquare).type);
        }
        return statement;
    }
}
