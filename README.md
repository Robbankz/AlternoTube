# AlternoTube
Alt-tech video platform front end for Android (WIP; PRIVATE USE ONLY).

AlternoTube is currently in very early development stages and is currently only intended for private use.

<div align="center" style="width:100%; display:flex; justify-content:space-between;">
<img src="https://user-images.githubusercontent.com/50536495/194220447-7c3ebdf3-8fc5-45a7-922f-27984672004c.png">

<img src="https://user-images.githubusercontent.com/50536495/194220581-b952148f-2f43-42e1-9819-54dc8faf154d.png">
</div>

# About

**Please do note that AlternoTube is a work in progress.**

I am quite passionate about alternative-tech (not a terrible fan of the name 'alternative-tech') video sharing sites like Odysee/LBRY, Rumble, and BitChute, this is because:

- They are less censorious; YouTube has a very tough grip on what's allowed and not allowed to be said
- They are not owned by Google or any large corporation
- Competition is always good, I think YouTube needs competitors so that their power is reduced, **YouTube is a monopoly**

The issue is that sites like Odysee, Rumble (not sure about BitChute) are really invasive privacy wise. I have studied through Rumble's source and they are using proprietary tracking software such as Google Analytics, I haven't yet studied Odysee's source although there are concerns in terms of how safe their site is privacy wise.

This is not to say I do not like these sites, I think all alternative-tech video sharing sites have a bright future ahead of them, especially if the content becomes less political and they do not become 'compromised' over time by bad players.

My goal with this project is to create a one hundred percent private, all-in-one frontend to these sites. Since no one has done anything similar on GitHub -- I think now is the time that a frontend for alternative-tech video sharing sites is done.

NewPipe has rejected adding alternative-tech support for -- in my opinion -- dubious reasons, the same with LibreTube. This is of course their right, but it is something which has bothered me, so I got inspired to create my own frontend for these sites.

Currently, I am scraping the content, so no JavaScript runs and thus privacy is improved by a lot. The issue is, the remote server can still know your IP address -- even if you simply make a request for an HTML page. Thus, we can improve privacy even more with Tor (Onion Routing), which is why I am focusing on adding native Tor support from inside the app. Due to the fact that Tor most certainly can be slow, this will be a togglable option.

High-level Tor overview (I'm also still learning):

<img src="https://cdn.arstechnica.net/wp-content/uploads/2014/01/tor-structure.jpg" alt="See the source image"/>
