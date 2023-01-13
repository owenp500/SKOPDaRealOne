public enum State { IDLE(0), MOVE(1), DEFEND(2), LOW_IDLE(3), ATTACK(7), STUN(6);
	public int value = 0;
	private State(int value) {
		this.value = value;
	}
};