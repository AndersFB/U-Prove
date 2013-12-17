<%--
  Created by IntelliJ IDEA.
  User: andersfogbunzel
  Date: 15/12/13
  Time: 20.55
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String url = uprove.Helper.getServletPath();
%>
<html>
<head>
    <title>Prover</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.yeti.css" rel="stylesheet">
    <link href="../../css/custom.yeti.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Prover</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Navigation <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<%= url %>/prover">Prover</a></li>
                        <li><a href="<%= url %>/prover/generate-tokens">Generate tokens</a></li>
                        <li class="divider"></li>
                        <li><a href="<%= url %>/token-wallets">Token wallets</a></li>
                        <li class="divider"></li>
                        <li><a href="<%= url %>/third-party">Third party (wayf)</a></li>
                        <li class="divider"></li>
                        <li><a href="<%= url %>/service-provider/car-rental">Car rental (Verifier)</a></li>
                        <li><a href="<%= url %>/service-provider/elections">Elections (Verifier)</a></li>
                        <li><a href="<%= url %>/service-provider/danish-tax">Danish tax (Verifier)</a></li>
                        <li><a href="<%= url %>/service-provider/sign-doc">Document signing (Verifier)</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-lg-6">
            <h1>Ready to generate tokens</h1>

            <p>We received the following information about you:</p>

            <p>Name: <%= request.getParameter("attribute_1") %>
            </p>

            <p>Age: <%= request.getParameter("attribute_2") %>
            </p>

            <p>Citizenship: <%= request.getParameter("attribute_3") %>
            </p>

            <form class="form-horizontal" action="<%= url %>/ProverServlet" method="post">
                Number of tokens to generate:

                <input type="text" name="numberOfTokens" placeholder="(ex. 5)" style="margin-left: 10px;">
                <input type="hidden" name="attribute_1" value="<%= request.getParameter("attribute_1") %>"/>
                <input type="hidden" name="attribute_2" value="<%= request.getParameter("attribute_2") %>"/>
                <input type="hidden" name="attribute_3" value="<%= request.getParameter("attribute_3") %>"/>
                <input type="hidden" name="numberOfAttributes"
                       value="<%= request.getParameter("numberOfAttributes") %>"/>
                <input type="hidden" name="message" value="init"/>

                <div class="form-group" style="margin-top: 10px;">
                    <div class="col-lg-6">
                        <button type="submit" class="btn btn-primary">Submit information</button>
                    </div>
                </div>
                </fieldset>
            </form>
        </div>
        <div class="col-lg-4">
            <h3>And then?</h3>

            <p>The prover have new received your information and is ready to generate tokens.</p>

            <p>How many tokens to generate do you decide, but a token can only be used once.</p>

            <p>If you ran out of tokens, you have to do this and the previous step again.</p>

            <p>When you login at a service provider do you chose what information you want to give the service
                provider.</p>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../../js/bootstrap.min.js"></script>
</body>
</html>