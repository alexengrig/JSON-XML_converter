StringReverser reverser=new StringReverser(){
@Override
public String reverse(String str){
        return new StringBuilder(str).reverse().toString();
        }
        };