* C-Minus Compilation to TM Code
* File: cMinusTestFiles/test.cm
* Standard prelude
0: LD  6,0(0)
1: LDA  5,0(6)
2: ST  0,0(0)
4: ST  0,-1(5)
5: IN  0,0,0
6: LD  7,-1(5)
7: ST  0,-1(5)
8: LD  0,-2(5)
9: OUT  0,0,0
10: LD  7,-1(5)
3: LDA  7,7(7)
12: ST  0,-1(5) store return address
13: LD  0,0(5) loading simpleVar
14: ST  0,-5(5) store value
15: LDC  0,0(0) loading int
16: ST  0,-6(5) store value
17: LD  1,-4(5) load 
18: SUB  0,1,0 perform operation 4
19: JEQ  0,2(7) br if true
20: LDC  0,0(0) false case
21: LDA  7,1(7) jump
22: LDC  0,1(0) true case
24: LD  0,-2(5) loading simpleVar
25: ST  0,-5(5) store value
26:	LDA  7,-3(7)  while jump
24: JEQ 7,2(7) jump forward
27: LD  7,-1(5) return back to caller
11: LDA 7,16(7) jump forward
29: ST  0,-1(5) store return address
30: LDA  0,-2(5) loading simpleVar
31: ST  0,-4(5) store value
32: ST  5,-6(5) push ofp
33: LDA  5,-6(5) push frame
34: LDA  0,1(7) load ac with ret ptr
35:	LDA  7,-32(7)  jump to main loc
36: LD  5,0(5) pop frame
37: LD  1,-4(5)  loading result
38: ST  0,0(1) assign: store value
39: LDA  0,-3(5) loading simpleVar
40: ST  0,-5(5) store value
41: ST  5,-7(5) push ofp
42: LDA  5,-7(5) push frame
43: LDA  0,1(7) load ac with ret ptr
44:	LDA  7,-41(7)  jump to main loc
45: LD  5,0(5) pop frame
46: LD  1,-5(5)  loading result
47: ST  0,0(1) assign: store value
48: LD  0,-2(5) loading simpleVar
49: ST  0,-5(5) store value
50: ST  5,-6(5) push ofp
51: LDA  5,-6(5) push frame
52: LDA  0,1(7) load ac with ret ptr
53:	LDA  7,-42(7)  jump to main loc
54: LD  5,0(5) pop frame
55: ST  5,-6(5) push ofp
56: LDA  5,-6(5) push frame
57: LDA  0,1(7) load ac with ret ptr
58:	LDA  7,-55(7)  jump to main loc
59: LD  5,0(5) pop frame
60: LD  7,-1(5) return back to caller
28: LDA 7,32(7) jump forward
61: ST  5,0(5) push ofp
62: LDA  5,0(5) push frame
63: LDA  0,1(7) load ac with ret ptr
64:	LDA  7,-36(7)  jump to main loc
65: LD  5,0(5) pop frame
66: HALT  0,0,0 
