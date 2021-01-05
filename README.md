# AdventOfCode (2020)
2020 is my first AoC, and my first attempt at speed coding.

Getting up at 5:40am each day, I'm pretty groggy to start off with.
Still, I'm having a crack at coding fast, not clean, and then learning
what I should be doing to get faster at it.

I'm sticking with Java this year (as I have too much else going on ATM).
I have used https://projecteuler.net/ in the past
to dabble in other languages (mainly Python there)
In a future AoC I'd be keen to try some Julia, Rust, Kotlin, Scala 3...

## Executing

Requires Java 15, as the new `record` construct (preview feature)
was used in a few places.

To build and profile all puzzles:

`./gradlew run`

## Performance summary

Since completing the 2020 challenge, I've now retrofitted something of a
test/profile framework so that I can iterate on the performance of some
of the interesting problems and perhaps learn a few tricks.

Current Timings:

```
Name                                                 |  Total μs |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+-----------+----------+----------+---------
Day 01 Report Repair                                 |        16 |       15 |        1 |        1 *
Day 02 Password Philosophy                           |        55 |       54 |        0 |        0 *
Day 03 Toboggan Trajectory                           |        11 |        8 |        1 |        2 *
Day 04 Passport Processing (input validation)        |        65 |       64 |        0 |        0 *
Day 05 Binary Boarding                               |         7 |        1 |        5 |        1 *
Day 06 Custom Customs                                |       213 |      106 |        - |      107
Day 07 Handy Haversacks (recursive bags)             |    14,337 |      792 |   13,535 |       11
Day 08 Handheld Halting (trivial asm)                |       326 |      116 |        3 |      206
Day 09 Encoding Error                                |       116 |       70 |       17 |       29
Day 10 Adapter Array                                 |         4 |        2 |        0 |        2 *
Day 11 Seating System (cellular automa 1)            |     2,098 |       33 |        - |    2,065 *
Day 12 Rain Risk (grid navigation)                   |        47 |       28 |        - |       20
Day 13 Shuttle Search                                |        16 |       12 |        1 |        4
Day 14 Docking Data (Floating bit-masks)             |    13,138 |       56 |   10,840 |    2,242
Day 15 Rambunctious Recitation (elf memory game)     |   236,309 |        6 |        8 |  236,295 *
Day 16 Ticket Translation                            |     1,555 |      699 |      219 |      638
Day 17 Conway Cubes (part 2 only)                    |     6,723 |       22 |        - |    6,702 *
Day 18 Operation Order (expression evaluation)       |     1,034 |       76 |      483 |      475
Day 19 Monster Messages (lexical token recognition)  |    32,414 |      618 |    4,424 |   27,372
Day 20 Jurassic Jigsaw (monochrome image processing) |    23,292 |      524 |   22,260 |      508
Day 21 Allergen Assessment (logic)                   |       935 |      143 |      780 |       11
Day 22 Crab Combat (recursive card game)             |    62,730 |      230 |       57 |   62,443
Day 23 Crab Cups (custom linked list)                |   770,401 |        7 |       32 |  770,362
Day 24 Lobby Layout (cellular automa 3)              |     9,562 |      482 |        8 |    9,073
Day 25 Combo Breaker                                 |     2,981 |       13 |    2,968 |        -

TOTAL: 1,178,584 μs
```

I've marked with `*` the ones that I've come back to optimise after solving the puzzle

Measurement notes:
 * Run on a i9-9980HK laptop (AC powered)
    * Base clock = 2.4 GHz, but this chip boosts very high so expect variance / randomness from this
      (TODO: stats)
 * "warm" timings, as this is the metric of most interest to me.
   (If the code was used anywhere that performance matters it would be JIT compiled.)
 * Excludes File-IO. (data is read into a `byte[]` before measurement)
 * The measurements include all parsing and puzzle-specific computation. E.g. if there is a Reg-Ex
   pattern, this is never cached in a static. I'm careful to perform all `Pattern.compile` calls
   each time for a fair comparison.

Still TODO:
  * Some indication of variance. (I'm collecting a progressing of measurements over time,
    but currently just reporting the last.)

