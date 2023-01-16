
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
		
		if (universeCount == 1) {
			this.current = new StartUniverse();
		}
		
		if (universeCount == 2) {
			return new GameUniverse();
		}
		
		if (universeCount == 3) {
			return new AIUniverse();
		}
		else {
			return this.current;
		}

	}
	
}
