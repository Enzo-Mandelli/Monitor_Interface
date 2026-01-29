package server;

public class DataFiltering {
    public static String[] sliceStr(String txt, int index){
        String list = "";
        txt = txt.replace("[", "");
        txt = txt.trim();
        char aux = '\'';
        int cont = 0;
        String[] error = new String[5];
        try {
            for (int i = 0; i < txt.length(); i++) {
                if(cont > index) break;
                if (txt.charAt(i) == (']')){
                    cont++;
                    i++;
                }
                if(index == cont){
                    txt = txt.replaceFirst(",", "");
                    for(int j = i; j < txt.length(); j++){
                        if(txt.charAt(j) == ']')break;
                        list = list + txt.charAt(j);

                    }
                    return list.split(",");
                }
            } //retorna a lista de itens em determinado index
        } catch (Exception e) {
        }
        return error = txt.split(",");
    }


    public static boolean formatType(String txt, String type){
        boolean correctType = false;
        switch (type) {
            case "i":
                try{
                    Integer.parseInt(txt);
                }catch (Exception e){
                    System.out.println("NOT A INTEGER");
                }
                break;
            case "f":
                try {
                    Float.parseFloat(txt);
                }catch (Exception e){
                    System.out.println("NOT A FLOAT");
                }
                break;
            case "b":
                    if(txt.equals("t") || txt.equals("1")) correctType = true;
                break;
        }
        return  correctType;
    }


    public static int contVar(String data){
        String[] dataList = sliceStr(data,0);
        return dataList.length;
    }
}
