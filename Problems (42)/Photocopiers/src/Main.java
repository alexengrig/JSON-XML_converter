// Posted from EduTools plugin
/**
 * Class to work with
 */
class Multiplicator {

	public static <T extends Copy<T>> Folder<T>[] multiply(Folder<T> folder, int arraySize) {
		final Folder<T>[] folders = new Folder[arraySize];
		for (int i = 0; i < arraySize; i++) {
			final Folder target = new Folder<>();
			target.put(folder.get().copy());
			folders[i] = target;
		}
		return folders;
	}

}
