# Wowsim
A sparetime project for simulating our implementation of players with some spells of the Classic World of Warcraft

Example of a typical spell (Corruption):

![alt tag](http://i.imgur.com/OxgcELt.png)

And another spell (Shadowbolt):

![alt tag](http://i.imgur.com/rFkPlpo.png)


Example printout of a simulation with these two Spells done by a level 36 Warlock over 200 deciseconds (20 seconds).
```
decisecond: 0
Casting Corruption
decisecond: 1
decisecond: 2
decisecond: 3
decisecond: 4
decisecond: 5
..
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
decisecond: 45
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
decisecond: 75
decisecond: 76
decisecond: 77
decisecond: 78
decisecond: 79
decisecond: 80
Shadowbolt dealt 217 damage
Corruption tick(2/6): 54 damage
Casting Shadowbolt
decisecond: 81
decisecond: 82
decisecond: 83
decisecond: 84
decisecond: 85
..
decisecond: 105
decisecond: 106
decisecond: 107
decisecond: 108
decisecond: 109
decisecond: 110
Shadowbolt dealt 217 damage
Corruption tick(3/6): 54 damage
Casting Shadowbolt
decisecond: 111
decisecond: 112
decisecond: 113
decisecond: 114
decisecond: 115
..
decisecond: 135
decisecond: 136
decisecond: 137
decisecond: 138
decisecond: 139
decisecond: 140
Shadowbolt dealt 217 damage
Corruption tick(4/6): 54 damage
Casting Shadowbolt
decisecond: 141
decisecond: 142
decisecond: 143
decisecond: 144
decisecond: 145
..
decisecond: 165
decisecond: 166
decisecond: 167
decisecond: 168
decisecond: 169
decisecond: 170
Shadowbolt dealt 217 damage
Corruption tick(5/6): 54 damage
Casting Shadowbolt
decisecond: 171
decisecond: 172
decisecond: 173
decisecond: 174
decisecond: 175
..
decisecond: 195
decisecond: 196
decisecond: 197
decisecond: 198
decisecond: 199
decisecond: 200
Shadowbolt dealt 217 damage
Corruption tick(6/6): 54 damage
Corruption faded
Total Damage Done: 1629.0
Dps: 81.44999999999999
[Corruption, Shadowbolt*6]
```

This program can figure out the best way to use your abilities.

The example shows that you should do one Corruption and then Shadowbolt six times.