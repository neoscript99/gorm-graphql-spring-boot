package ns.flex.util
{
	import mx.effects.IEffect;
	import mx.effects.Move;
	import mx.effects.Sequence;
	
	public class EffectUtil
	{
		static public function createMove(properties:Object, target:Object=null):Move
		{
			var move:Move=new Move(target);
			ObjectUtils.copyProperties(move, properties);
			return move;
		}
		
		static public function createSequence(properties:Object, target:Object,
			... actions):Sequence
		{
			var seq:Sequence=new Sequence();
			seq.target=target;
			ObjectUtils.copyProperties(seq, properties);
			
			for each (var effect:IEffect in actions)
				seq.addChild(effect);
			return seq;
		}
	}
}