package org.owasp.webgoat.plugin.challenge1;

import lombok.SneakyThrows;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.plugin.Flag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;

import static org.owasp.webgoat.plugin.SolutionConstants.PASSWORD;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since August 11, 2016
 */
@AssignmentPath("/challenge/1")
public class Assignment1 extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    AttackResult completed(@RequestParam String username, @RequestParam String password, HttpServletRequest request) throws IOException {
        boolean ipAddressKnown = checkClientOrigin(request);
        boolean passwordCorrect = "admin".equals(username) && PASSWORD.equals(password);
        if (passwordCorrect && ipAddressKnown) {
            return success().feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(1)).build();
        } else if (passwordCorrect) {
            return failed().feedback("ip.address.unknown").build();
        }
        return failed().build();
    }

    @SneakyThrows
    private boolean checkClientOrigin(HttpServletRequest request) {
        InetAddress ip = InetAddress.getLocalHost();
        return getClientIP(request).contains(ip.getHostAddress());
    }

    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
