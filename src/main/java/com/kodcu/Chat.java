package com.kodcu;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: usta
 * Date: 07.04.2013
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */
@Path("/chat")
public class Chat {

    final static Map<String, AsyncResponse> beklemeKuyrugu = new ConcurrentHashMap<>();
    final static ExecutorService ex = Executors.newSingleThreadExecutor();

    @Path("/{nick}")
    @GET
    @Produces("text/plain")
    public void askidaKal(@Suspended AsyncResponse asyncResp, @PathParam("nick") String nick) {

        beklemeKuyrugu.put(nick, asyncResp);
    }

    @Path("/{nick}")
    @POST
    @Produces("text/plain")
    @Consumes("text/plain")
    public String mesajGonder(final @PathParam("nick") String nick, final String mesaj) {

        ex.submit(new Runnable() {
            @Override
            public void run() {
                Set<String> nicks = beklemeKuyrugu.keySet();
                for (String n : nicks) {
                    // Mesaj gondericisinden haric herkese gonderilir.
                    if (!n.equalsIgnoreCase(nick))
                        beklemeKuyrugu.get(n).resume(nick + " dedi ki: " + mesaj);
                }
            }
        });

        return "Mesaj gönderildi..";
    }
}
