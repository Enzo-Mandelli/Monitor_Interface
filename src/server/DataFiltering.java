package server;

public class DataFiltering {
    public static String[] sliceStr(String txt, int index){
        String lista = "";
        try {
            int cont = index + 1;
            for (int i = 1; i < txt.length(); i++) {
                if (txt.charAt(i) == (']')) cont++;
                if (txt.charAt(i) != '[' && cont == index && txt.charAt(i) != ']') {
                    lista = lista + txt.charAt(i);
                }
            } //retorna a lista de itens em determinado index
        } catch (Exception e) {
            System.out.println("erro no fatiamento");
        }
        return lista.split(",");
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
