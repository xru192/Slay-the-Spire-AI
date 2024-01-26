# Slay the Spire AI

Slay the Spire mod which has a program play the game. Provides an interface for creating one's own AI to play the game.

## Implementation details

### Combat Simulator

In order to make in-combat decisions, the AI simulates playing different combinations of cards, and then evaluates the
resulting states.
A fully complete simulator would accurately simulate the playing of any card in any situation. However, there is a lot
of
functionality which must be captured (cards, relics, monsters, potions).
So the simulator is a work in progress, where functionality is implemented as it becomes a priority.

[More on Simulator implementation](src/main/java/newaimod/util/simulator/README.md)

## Milestones

Some targets to aim for when it comes implementing and improving the AI. For now, the focus is entirely on an AI for
Ironclad on Ascension 0/1.

### Performance (Basic)

- [X] Consistently (95+%) reaches Act I boss
- [ ] Often (80+%) defeats Act I boss
- [ ] Frequently (50+%) reaches Act II boss
- [X] Occasionally (20+%) defeats Act II boss

### Performance (Decent)

- [ ] Always reaches Act I boss
- [ ] Consistently (95+%) defeats Act I boss
- [ ] Often (80+%) reaches Act II boss
- [ ] Usually (70+%) defeats Act II boss
- [ ] Frequently (50+%) reaches Act III boss
- [ ] Occasionally (20+%) defeats Act III boss

### Performance (Advanced)

- [ ] Always reaches Act I boss
- [ ] Consistently (95+%) defeats Act I boss
- [ ] Consistently (95+%) reaches Act II boss
- [ ] Very often (90+%) defeats Act II boss
- [ ] Often (80+%) reaches Act III boss
- [ ] Usually (70+%) defeats Act III boss
- [ ] Occasionally (20+%) defeats Act IV boss

### Performance (Amazing)

- [ ] Always reaches Act I boss
- [ ] Consistently (95+%) defeats Act I boss
- [ ] Consistently (95+%) reaches Act II boss
- [ ] Very often (90+%) defeats Act II boss
- [ ] Very often (90+%) reaches Act III boss
- [ ] Often (80+%) defeats Act III boss
- [ ] Frequently (50+%) defeats Act IV boss

## Run history

| Version | Runs | Average Floors Cleared | Reached Act I Boss | Defeated Act I Boss | Reached Act II Boss | Defeated Act II Boss | Reached Act III Boss | Defeated Act III Boss |
|:-------:|:----:|:----------------------:|:------------------:|:-------------------:|:-------------------:|:--------------------:|:--------------------:|:---------------------:|
|  0.0.0  |  20  |          23.4          |         18         |         11          |          6          |          1           |          0           |           0           |
|  0.0.1  |  20  |         28.65          |         20         |         13          |          9          |          4           |          4           |           3           |
