var a = { foo: 23 };
var i = 0;
while(i<500000000) {
    i++;
    if(i % 100000000 == 0) {
        print(i);
    }
}
print("Hello Nashorn!");