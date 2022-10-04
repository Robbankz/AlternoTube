# AlternoTube
Alt-tech video platform front end for Android (WIP; PRIVATE USE ONLY).

# About

**Please do note that AlternoTube is a work in progress.**

I am quite passionate about alternative-tech video sharing sites like Odysee/LBRY, Rumble, and BitChute, this is because:

- They are less censorious; YouTube has a very tough grip on what's allowed and not allowed to be said
- They are not owned by Google or any large corporation
- Competition is always good, I think YouTube needs competitors so that their power is reduced

The issue is that sites like Odysee, Rumble (not sure about BitChute) are really invasive privacy wise. I have studied through Rumble's source and they are using proprietary tracking software such as Google Analytics, I haven't yet studied Odysee's source although there are concerns in terms of how safe their site is privacy wise.

This is not to say I do not like these sites, I think all alternative-tech video sharing sites have a bright future ahead of them, especially if the content becomes less political.

My goal with this project is to create a one hundred percent private, all-in-one frontend to these sites. Currently, I am scraping the content, so no JavaScript runs and thus privacy is improved by a lot. The issue is, the remote server can still know your IP address -- even if you simply make request for an HTML page. Thus, we can improve privacy even more with Tor (Onion Routing), which is why I am focusing on adding native Tor support from inside the app. Due to the fact that Tor most certainly can be slow, this will be a togglable option.

High-level Tor overview (I'm also still learning):

<img src="https://cdn.arstechnica.net/wp-content/uploads/2014/01/tor-structure.jpg" alt="See the source image"/>
