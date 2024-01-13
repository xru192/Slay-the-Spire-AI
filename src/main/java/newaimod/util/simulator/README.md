# Combat Simulator

In order to make in-combat decisions, the AI simulates playing different combinations of cards, and then evaluates the
resulting states.
A fully complete simulator would accurately simulate the playing of any card in any situation. However, there is a lot
of
functionality which must be captured (cards, relics, monsters, potions).
So the simulator is a work in progress, where functionality is implemented as it becomes a priority.

## Functionality

### Cards

When cards are played, their effects should be executed. Many cards have unique and/or multiple effects, so each card
must be implemented separately. Below is a summary of the status of the implementation of each card.

| Card          | Category | Implementation Status | Notes (* - unfinished)                               |
|---------------|----------|-----------------------|------------------------------------------------------|
| Bash          | Attack   | Done                  | Vulnerable                                           |
| Carnage       | Attack   | Partial               | Ethereal*                                            |
| Cleave        | Attack   | Done                  | Multi-attack                                         |
| Headbutt      | Attack   | Partial               | Putting card on top*                                 |
| Iron Wave     | Attack   | Done                  | Block                                                |
| Pommel Strike | Attack   | Done                  | Draw card(s)                                         |
| Strike        | Attack   | Done                  |                                                      |
| Twin Strike   | Attack   | Done                  | Multi-attack                                         |
| Uppercut      | Attack   | Done                  | Weak, Vulnerable                                     |
| Whirlwind     | Attack   | Done                  | Cost-X, Multi-attack                                 |
| Inflame       | Power    | Done                  | Strength                                             |
| Armaments     | Skill    | Partial               | Choose a card to upgrade*, Upgrade all cards in hand |
| Battle Trance | Skill    | Done                  | Draw cards, no-draw this turn                        |
| Defend        | Skill    | Done                  |                                                      |
| Flame Barrier | Skill    | Partial               | Flame Barrier effect                                 |
| Shrug it Off  | Skill    | Partial               | Draw card                                            |
| Slimed        | Status   | Done                  | Exhaust                                              |

### Relics

When the player possesses a relic which affects combat, the relic's effects should be executed. Below is a summary of
the status of the implementation of each relic.

| Relic              | Implementation Status | Notes (* - unfinished)                                                      |
|--------------------|-----------------------|-----------------------------------------------------------------------------|
| Akabeko            | Unimplemented         | 8 extra damage on first attack is unknown*                                  |
| Anchor             | Done                  | Player's block at start of turn is known                                    |
| Ancient Tea Set    | Done                  | Player's energy at start of turn is known                                   |
| Art of War         | Unimplemented         | Bonus to not playing attacks is unknown*                                    |
| Bag of Marbles     | Done                  | Monster's vulnerable status at start of turn is known                       |
| Blood Vial         | Done                  | Player's health at start of turn is known                                   |
| Bronze Scales      | Unimplemented         | Player's thorns is unknown*                                                 |
| Centennial Puzzle  | Unimplemented         | Drawing cards is unimplemented*                                             |
| Happy Flower       | Unimplemented         | Counter on relic is unknown*, but player's energy at start of turn is known |
| Lantern            | Done                  | Player's energy at start of turn is known                                   |
| Nunchaku           | Unimplemented         | Counter on relic is unknown*, but player's energy is known                  |
| Oddly Smooth Stone | Done                  | Player's dexterity is known                                                 |
| Orichalcum         | Unimplemented         | End of turn effect is unknown*                                              |
| Pen Nib            | Unimplemented         | Counter on relic is unknown*, and doubled damage is unknown*                |
| The Boot           | Unimplemented         | Damage effect is unknown*                                                   |
| Toy Ornithopter    | Unimplemented         | Healing effect is unknown*                                                  |
| Vajra              | Done                  | Player's strength is known                                                  |
| Red Skull          | Unimplemented         | Conditional strength is unknown*, but player strength is known in general   |
| Blue Candle        | Unimplemented         | Playable curses are unknown*                                                |
| Gremlin Horn       | Unimplemented         | Proc on kill is unknown*                                                    |
| Horn Cleat         | Done                  | Player's block at start of turn is known                                    |
| Ink Bottle         | Unimplemented         | Counter on relic is unknown*, and drawing cards is unimplemented*           |
| Kunai              | Unimplemented         | Counter is unknown*                                                         |
| Letter Opener      | Unimplemented         | Counter is unknown*                                                         |
| Meat on the Bone   | Unimplemented         | Condition is unknown*                                                       |
| Mercury Hourglass  | Unimplemented         | Damage in future turns unknown*                                             |
| Mummified Hand     | Unimplemented         | Condition is unknown*, but cards 0 cost-for-turn is known                   |
| Ornamental Fan     | Unimplemented         | Condition is unknown*                                                       |
| Shuriken           | Unimplemented         | Counter is unknown*                                                         |
| Strike Dummy       | Unimplemented         | Extra damage is unknown*                                                    |
| Sundial            | Unimplemented         | Condition is unknown*, but player energy is known                           |
| White Beast Statue | Unimplemented         | Reward is unknown*                                                          |
| Paper Phrog        | Unimplemented         | Effect is unknown*                                                          |
| Self-Forming Clay  | Unimplemented         | Effect is unknown*                                                          |
| Bird-Faced Urn     | Unimplemented         | Effect is unknown*                                                          |
| Calipers           | Unimplemented         | Effect is unknown*                                                          |
| Dead Branch        | Unimplemented         | Effect is unknown*                                                          |                                                                            
| Du-Vu Doll         | Done                  | Player strength is known                                                    |
| Fossilized Helix   | Unimplemented         | Buffer is unknown*                                                          |
| ...                |                       |                                                                             |

### Monsters

Many monsters have unique abilities (e.g. The Guardian's defensive form).
These must be implemented in the simulator to be reflected.
By default, only monsters' current health and block are seen by the AI.
