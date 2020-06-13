# Streamer

### emit("init", my_id);

return my_id;

### emit("createStreamRoom", streamer_id);

return nothing;



### on("request", data)

data.to = streamer_id

data.from = listener_id



### emit("call", {to : listener_id, sdp : sdp(offer), from : streamer_id})



### on("call", data)

if data.sdp 가 있으면, peerconnection 에 sdp 삽입

​	data.sdp == offer , emit("call", {to : streamer_id, sdp : sdp(answer), listener_id})

else, peerconnection 에 candidate 정보 삽입

​	if candiate == null, end;

### emit("call", {to : listener_id, candidiate : candidate, from : streamer_id})



### emit("peerStream", stream_inform)

### On("peerStream", data)

------

# Listener

### emit("init", my_id)

return my_id

### emit("request", {to : streamer_id, from : listener_id})

return nothing

### 

### on("call", data)

data.sdp 가 있으면 peerconnection 에 삽입

data.sdp == offer , emit("call", {to : streamer_id, sdp : sdp(answer), listener_id})



### emit("peerStream", stream_inform)

### On("peerStream", data)p

