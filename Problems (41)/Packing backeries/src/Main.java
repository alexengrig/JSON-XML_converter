// Posted from EduTools plugin
/**
    This packer has too much freedom and could repackage stuff in wrong direction.
    Fix method types in signature and add implementation. 
*/
class Packer {
	public void repackage(Box<? super Bakery> to, Box<? extends Bakery> from) {
		to.put(from.get());
	}

}