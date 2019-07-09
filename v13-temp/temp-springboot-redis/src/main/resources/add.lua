local bloonName = KEY[1];
local value =KEY[2];

local result = redis.call('bf.add',bloonName,value);
return result