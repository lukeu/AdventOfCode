# AdventOfCode
2020 was my first AoC, and my first attempt at speed coding. I stuck with Java, although I would
like to dabble in other languages if I had less going on.

The goal was to code fast (not clean) and then learn what I should be doing to get faster at it.
Some days I'd clean up the code a bit. Then perhaps what became more interesting was optimising the
code for runtime performance - comparing my initial 'naive' runs against other implementations
(notably in Rust) and optimising certain days to compete with heavily-optimised C++/ASM code.
After dropping to parse bytes instead of char/String, Java became surprisingly competitive with
the native-language implementations (which were doing similar low-level hackery).

## Executing

Requires Java 17 (and soon probably 21). To build and profile all puzzles:

`./gradlew run`

## Performance summary

Since completing the 2020 challenge, I retrofitted something of a test-and-profile framework so
that I can iterate on the performance of some of the interesting problems and learn a few tricks.

I've marked with `*` the ones that I've come back to optimise after solving the puzzle. Generally,
the before-and-after versions of the optimisation can be found in the git history.

Current Timings - WARM (after 5 runs)

```
YEAR 2020                                            |  Total μs |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+-----------+----------+----------+---------
Day 01 Report Repair                                 |        28 |       27 |        1 |        1 *
Day 02 Password Philosophy                           |        68 |       66 |        1 |        1 *
Day 03 Toboggan Trajectory                           |        14 |       11 |        1 |        2 *
Day 04 Passport Processing (input validation)        |        82 |       81 |        1 |        0 *
Day 05 Binary Boarding                               |        11 |        4 |        7 |        1 *
Day 06 Custom Customs                                |       261 |      143 |        - |      118
Day 07 Handy Haversacks (recursive bags)             |    17,687 |    1,040 |   16,634 |       13
Day 08 Handheld Halting (trivial asm)                |       441 |      140 |        4 |      297
Day 09 Encoding Error                                |       148 |       88 |       21 |       39
Day 10 Adapter Array                                 |         9 |        6 |        1 |        3 *
Day 11 Seating System (cellular automa 1)            |     2,490 |       46 |        - |    2,444 *
Day 12 Rain Risk (grid navigation)                   |        54 |       31 |        - |       23
Day 13 Shuttle Search                                |        17 |       12 |        1 |        5
Day 14 Docking Data (Floating bit-masks)             |    14,362 |       31 |   12,286 |    2,045
Day 15 Rambunctious Recitation (elf memory game)     |   187,018 |        9 |        8 |  187,001 *
Day 16 Ticket Translation                            |     1,633 |      680 |      253 |      701
Day 17 Conway Cubes (part 2 only)                    |     6,604 |       28 |        - |    6,576 *
Day 18 Operation Order (expression evaluation)       |     1,157 |       34 |      564 |      560
Day 19 Monster Messages (lexical token recognition)  |    36,483 |      451 |    5,159 |   30,874
Day 20 Jurassic Jigsaw (monochrome image processing) |    25,360 |      501 |   24,285 |      574
Day 21 Allergen Assessment (logic)                   |     1,004 |      132 |      859 |       13
Day 22 Crab Combat (recursive card game)             |    66,044 |       22 |       56 |   65,966
Day 23 Crab Cups (custom linked list)                |   181,125 |       20 |        9 |  181,096 *
Day 24 Lobby Layout (cellular automa 3)              |     9,771 |      392 |        8 |    9,372
Day 25 Combo Breaker                                 |     4,264 |       14 |    4,251 |        -
-----------------------------------------------------+-----------+----------+----------+---------

YEAR 2023                                            |  Total μs |  Parsing |   Part 1 |   Part 2
-----------------------------------------------------+-----------+----------+----------+---------
Day 01 Trebuchet                                     |     1,583 |       50 |      393 |    1,140
Day 02 Cube Conundrum                                |         4 |        3 |        1 |        0
Day 05 If You Give A Seed A Fertilizer               |       301 |      212 |       15 |       74
Day 11 Cosmic Expansion                              |       745 |       53 |      338 |      353
Day 12 Hot Springs                                   |     5,824 |    1,072 |      304 |    4,448 *
-----------------------------------------------------+-----------+----------+----------+---------

TOTAL: 564,593 μs

```

The timings were taken using JDK 17 (Temurin) on a i9-9980HK laptop (AC powered)
  * Base clock = 2.4 GHz. This chip boosts very high (> 5 GHz) so expect high variance.
    (Unfortunately I cannot lock the CPU frequency on this device.)
* "warm" timings, as this is the metric of most interest to me.
  (If the code was used anywhere that performance matters it would be JIT compiled.)
* Excludes File-IO. (data is read into a `byte[]` before measurement)
* The measurements include all parsing and puzzle-specific computation.
  For example, a Reg-Ex would never be cached in a static - I would ensure `Pattern.compile`
  is called and included in the timing for a fair comparison.

