# wowsim
A sparetime project for simulating our implementation of players with some spells of the Classic World of Warcraft

Example of a typical spell (Corruption):

![alt tag](http://i.imgur.com/OxgcELt.png)

And another spell (Shadowbolt):

![alt tag](http://i.imgur.com/gIiTpT2.png)


Example printout of a simulation with these two Spells done by a level 36 Warlock over 100 deciseconds (10 seconds).
```
decisecond: 0
Casting Corruption
decisecond: 1
decisecond: 2
decisecond: 3
decisecond: 4
decisecond: 5
decisecond: 6
decisecond: 7
decisecond: 8
decisecond: 9
decisecond: 10
decisecond: 11
decisecond: 12
decisecond: 13
decisecond: 14
decisecond: 15
decisecond: 16
decisecond: 17
decisecond: 18
decisecond: 19
decisecond: 20
Corruption applied to target
Casting Shadowbolt
decisecond: 21
decisecond: 22
decisecond: 23
decisecond: 24
decisecond: 25
..
decisecond: 46
decisecond: 47
decisecond: 48
decisecond: 49
decisecond: 50
Shadowbolt dealt 217 damage
Corruption tick(1/6): 54 damage
Casting Shadowbolt
decisecond: 51
decisecond: 52
decisecond: 53
decisecond: 54
decisecond: 55
..
decisecond: 74
decisecond: 75
decisecond: 76
decisecond: 77
decisecond: 78
decisecond: 79
decisecond: 80
Shadowbolt dealt 217 damage
Corruption tick(2/6): 54 damage
decisecond: 81
decisecond: 82
decisecond: 83
decisecond: 84
decisecond: 85
decisecond: 86
decisecond: 87
decisecond: 88
decisecond: 89
decisecond: 90
decisecond: 91
decisecond: 92
decisecond: 93
decisecond: 94
decisecond: 95
decisecond: 96
decisecond: 97
decisecond: 98
decisecond: 99
decisecond: 100
Total Damage Done: 543.0
Dps: 54.3
[Corruption, Shadowbolt*2]
```

With this you can figure out what the best way to use your abilities are.
The example shows that you should do one Corruption and then Shadowbolt twice.