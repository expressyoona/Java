#How to play this game
1. Configure server's IP(Line 300 in TTTClient.java). If server run on your computer, you don't need change the host.
2. Run command lines below into this folder(Must be in order):
- Server:
  + javac TTTServer.java
  + java TTTServer
- Client(Maximum is 2 players play at the same time).
  + javac TTTClient
  + java TTTClient
- After each match, server will save the result in result.txt file on the folder that server locate.
- Remember: Terminate the server if you don't want to receive the "Port is already in use" error.
