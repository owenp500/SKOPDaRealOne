// Idle(0); Move(1); Defend(2); LowIdle(3); LowBlock(4); LowAttack(5); Stun(6); Attack(7)
public enum State { IDLE(0), MOVE(1), DEFEND(2), 
		LOW_IDLE(3),LOW_DEFEND(4),LOW_ATTACK(5),
		STUN(6),ATTACK(7);
	public int value = 0;
	private State(int value) {
		this.value = value;
	}
}