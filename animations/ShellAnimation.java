
public class ShellAnimation implements Animation {

	private static int universeCount = 0;
	private Universe current = null;
	
	public static int getUniverseCount() {
		return universeCount;
	}

	public static void setUniverseCount(int count) {
		ShellAnimation.universeCount = count;
	}

	public Universe getNextUniverse() {

		universeCount++;
		
//		if (universeCount == 2) {
//			this.current = new StartUniverse();
//		}
		
		if (universeCount == 1) {
			return new GameUniverse();
		}
		else {
			return null;
		}

	}
	
}
