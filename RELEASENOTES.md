#Split release notes

##0.3.2-SNAPSHOT (2015-06-21)
  - StringSplit.tokenizer renamed to StringSplit.split
  - split new parameter : maxcount, which give the maximum size of the vector of results
  - StringSplit.split performance enhancements (JVM 1.8.0_45 / scala 2.11.6 / zorglub) :
    * BEFORE : found 6 210 961 tokens/second through 517 580 lines/second 
    * AFTER  : found 9 494 324 tokens/second through 791 193 lines/second

##0.3.1
  - scala updates
  - scalatest updates


##2014-12-21
  - StringSplit.tokenizer performances enhancements
    * BEFORE : found 2 278 423 tokens/second through 189 868 lines/second 
    * AFTER  : found 5 405 444 tokens/second through 450 453 lines/second  (algorithm enhancements)
    
  - FlowGroup : reassemble performances enhancements 
    * BEFORE : found 173 983 entries/second through 1 043 898 lines/second
    * AFTER  : found 327 920 entries/second through 1 967 523 lines/second (just test enhancements in fact) 
