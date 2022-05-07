import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Supermercado {
    private static int quantidadeEstoque = 0;
    private static int quantidadeVendas = 0;
    private static Object[][] estoque = new Object[5][9];
    private static Object[][] vendas = new Object[5][4];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opcao;
        String opcoes = "0 1 2 3 4 5 6 7 8";
        do {
            while (true) {
                System.out.println("---------------------------------------------------------------------------");
                System.out.println("\nPor favor, selecione o item desejado");
                System.out.println("0 - Saida");
                System.out.println("1 - Cadastrar/Comprar produtos");
                System.out.println("2 - Imprimir estoque");
                System.out.println("3 - Listar os produto pelo Tipo");
                System.out.println("4 - Pesquisar um produto pelo codigo");
                System.out.println("5 - Pesquisar um produto pelo nome");
                System.out.println("6 - Vendas");
                System.out.println("7 - Relatorio de vendas analitico");
                System.out.println("8 - Relatorio de vendas sintetico\n");
                System.out.println("---------------------------------------------------------------------------");
                opcao = sc.next();

                if (opcao.charAt(0) == '0') {
                    break;
                }else if(opcoes.contains(opcao.substring(0, 1))){
                    sistema(Integer.parseInt(opcao));
                }else{
                    System.out.println("Informe um item valido!");
                }

            }
        } while (!opcoes.contains(opcao.substring(0, 1)));

        System.out.println("Saindo do sistema! Tchau, volte sempre!");
    }

    public static void sistema(int opcao) {
        Object[] infos;

        switch (opcao) {
            case 1:
                infos = inserirInfos();
                int status = verificarCadastro((String) infos[2],2,estoque,quantidadeEstoque);
                if (status == -1) {
                    quantidadeEstoque = cadastrar(infos, estoque, quantidadeEstoque);
                    //estoque inicia com 0
                    estoque[quantidadeEstoque - 1][8] = 0;
                } else {
                    atualizar(infos, status);
                }
                dadosDaCompra(infos);
                break;
            case 2:
                imprimirTabela(estoque, quantidadeEstoque);
                break;
            case 3:
                listarPorTipo();
                break;
            case 4:
                // pesquisar produto pelo codigo
                pesquisarPorCodigo();
                break;
            case 5:
                // pesquisar produto pelo contains
                pesquisarPorNome();
                break;
            case 6:
                // vendas
                entradaCliente();
                break;
            case 7:
                // relatorio analitico
                imprimirVendasAnalitico(vendas,quantidadeVendas);
                break;
            case 8:
                // relatorio com o cpf igual
                calcularVendasSintetico();
                break;
        }

    }

    public static Object[] inserirInfos() {
        Scanner sc = new Scanner(System.in);
        Object[] infos = new Object[6];

        // Verificar entrada do enum tipo
        do {
            try {
                System.out.println("Tipo: ");
                infos[0] = Tipo.valueOf(sc.nextLine());
                break;
            } catch (Exception ex) {
                System.out.println("Tipo invalido, digite novamente!");
            }
        } while (true);

        System.out.println("Marca: ");
        infos[1] = sc.nextLine();
        System.out.println("Identificador: ");
        infos[2] = sc.nextLine();
        System.out.println("Nome: ");
        infos[3] = sc.nextLine();

        // Verificar se eh positivo e se eh double
        do {
            try {
                System.out.println("Preco Custo: ");
                infos[4] = sc.nextDouble();
                if ((Double) infos[4] > 0.d) {
                    break;
                }
                System.out.println("Valor negativo invalido, digite novamente!");
            } catch (InputMismatchException ex) {
                System.out.println("Preco Custo invalido, digite novamente!");
                sc.next();
            }
        } while (true);

        // verificar entrada de quantidadeEstoque
        do {
            try {
                System.out.println("Quantidade: ");
                infos[5] = sc.nextInt();
                if ((Integer) infos[5] > 0) {
                    break;
                }
                System.out.println("Valor negativo invalido, digite novamente!");
            } catch (InputMismatchException ex) {
                System.out.println("Quantidade invalida, digite novamente!");
                sc.next();
            }
        } while (true);
        return infos;
    }

    public static int verificarCadastro(String identificador,int coluna, Object [][] matriz, int quantidade) {
        for (int i = 0; i < quantidade; ++i) {
            if (matriz[i][coluna].equals(identificador)) {
                return i;
            }
        }
        return -1;
    }

    public static int cadastrar(Object[] infos, Object[][] matriz, int quantidade) {
        if (quantidade == matriz.length) {
            redimensionarEficiente();
        }

        for (int i = 0; i < infos.length; ++i) {
            matriz[quantidade][i] = infos[i];
        }
        return ++quantidade;
    }

    public static void redimensionarEficiente() {
        Object[][] newArray = new Object[estoque.length * 2][estoque[0].length];

        for (int i = 0; i < estoque.length; ++i) {
            for (int i1 = 0; i1 < estoque[i].length; ++i1) {
                newArray[i][i1] = estoque[i][i1];
            }
        }

        estoque = newArray;
    }

    public static void atualizar(Object[] infos, int posicao) {
        for (int i = 0; i < infos.length; ++i) {
            estoque[posicao][i] = infos[i];
        }

    }

    public static void dadosDaCompra(Object[] infos) {
        // Data Compra
        LocalDateTime datetime1 = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formatDateTime = datetime1.format(format);
        estoque[quantidadeEstoque - 1][6] = formatDateTime;

        // Preco de venda
        String nomeTipo = String.valueOf(estoque[quantidadeEstoque - 1][0]);
        estoque[quantidadeEstoque - 1][7] = (Double) estoque[quantidadeEstoque - 1][4] * (Double) Tipo.valueOf(nomeTipo).getMarkup();

        // Estoque
        int quantidadeEstoque = (Integer) estoque[Supermercado.quantidadeEstoque - 1][8];
        int quantidadeCompra = (Integer) infos[5];
        estoque[Supermercado.quantidadeEstoque - 1][8] = quantidadeEstoque + quantidadeCompra;

    }

    public static void imprimirTabela(Object[][] estoque, int quantidade) {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-15s %-20s %-15s %-12s %-20s %-20s %-10s %n", "Tipo", "Marca", "Identificador", "Nome", "Preco Custo(R$)", "Quantidade", "Data Compra", "Preco de venda(R$)", "Estoque");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < quantidade; ++i) {
            System.out.format("%-10s %-20s %-15s %-20s %-15.2f %-12d %-20s %-20.2f %-10d %n", estoque[i][0], estoque[i][1], estoque[i][2], estoque[i][3], (Double) estoque[i][4], (Integer) estoque[i][5], estoque[i][6], (Double) estoque[i][7], (Integer) estoque[i][8]);
        }

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void listarPorTipo() {
        String opcao;
        Object[][] lista = new Object[quantidadeEstoque][estoque[0].length];
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Por favor, selecione o item desejado");
            for (int i = 0; i < Tipo.values().length; i++) {
                System.out.printf("%d - %s %n", i + 1, Tipo.values()[i]);
            }
            opcao = sc.next();
            if (opcao.equals("1") || opcao.equals("2") || opcao.equals("3")) {
                break;
            }
        } while (true);

        int contador = 0;
        for (int i = 0; i < quantidadeEstoque; i++) {
            if (estoque[i][0].equals(Tipo.values()[Integer.parseInt(opcao) - 1])) {
                for (int i1 = 0; i1 < estoque[i].length; i1++) {
                    lista[contador][i1] = estoque[i][i1];
                }
                contador++;
            }
        }
        imprimirTabela(lista, contador);
    }

    public static void pesquisarPorCodigo() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor, insira um identificador.");
        String identificador = sc.nextLine();

        Object[][] produto = new Object[1][estoque[0].length];
        for (int i = 0; i < quantidadeEstoque; i++) {
            if (estoque[i][2].equals(identificador)) {

                for (int i1 = 0; i1 < estoque[i].length; i1++) {
                    produto[0][i1] = estoque[i][i1];
                }
                imprimirTabela(produto, 1);
                return;
            }
        }
        System.out.println("Identificador nao encontrado!");
    }

    public static void pesquisarPorNome() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor, insira um Nome.");
        String nome = sc.nextLine();

        Object[][] produto = new Object[quantidadeEstoque][estoque[0].length];
        int quantidadeProduto = 0;
        for (int i = 0; i < quantidadeEstoque; i++) {
            if (((String) estoque[i][3]).contains(nome)) {
                for (int i1 = 0; i1 < estoque[i].length; i1++) {
                    produto[quantidadeProduto][i1] = estoque[i][i1];
                }
                quantidadeProduto++;
            }

        }
        if(quantidadeProduto > 0){
            imprimirTabela(produto, quantidadeProduto);
            return;
        }
        System.out.println("Nome nao encontrado!");
    }

    public static void entradaCliente() {
        Scanner sc = new Scanner(System.in);
        Object infosVendas[] = new Object[4];
        do {
            System.out.println("Deseja inserir o CPF/CNPJ?");
            System.out.println("S - Sim");
            System.out.println("N - Nao");
            String opcao = sc.nextLine();

            // verificar se opcao esta ok
            if (opcao.contains("S")) {
                infosVendas[0] = verificarCPF();
                infosVendas[1] = TipoCliente.values()[tipoDeCliente()];
                break;
            } else if (opcao.contains("N")) {
                infosVendas[0] = "00000000191";
                infosVendas[1] = TipoCliente.values()[0];
                break;
            } else {
                System.out.println("Opcao invalida, digite novamente!");
            }
        } while (true);
        venderProduto(infosVendas);
    }

    public static int tipoDeCliente() {
        Scanner sc = new Scanner(System.in);
        String tipoCliente;
        do {
            System.out.println("Que tipo de cliente?");
            for (int i = 0; i < TipoCliente.values().length; i++) {
                System.out.printf("%d - %s %n", i + 1, TipoCliente.values()[i]);
            }
            tipoCliente = sc.next();
            if (tipoCliente.equals("1") || tipoCliente.equals("2") || tipoCliente.equals("3")) {
                return Integer.parseInt(tipoCliente) - 1;
            }
        } while (true);
    }

    public static String verificarCPF() {
        Scanner sc = new Scanner(System.in);
        String cpf;
        do {
            System.out.println("Digite o CPF/CNPJ: ");
            cpf = sc.nextLine();
            if (cpf.length() != 11) {
                System.out.println("Erro na quantidadeEstoque de numeros, digite novamente!");
            }
        } while (cpf.length() != 11);
        return cpf;
    }

    public static void venderProduto(Object [] infosVendas) {
        Scanner sc = new Scanner(System.in);
        int linha = 0;
        Object produtosVendidos[] []= new Object[5][5];
        Object infosImprimir[] = new Object[5];
        int referenciaProduto;
        encerrarVenda:
        do {
            //verificar se identificador é valido
            do {
                System.out.println("Identificador ou fim para encerrar: ");
                infosImprimir[0] = sc.nextLine();
                if (((String) infosImprimir[0]).equalsIgnoreCase("FIM")) {
                    break encerrarVenda;
                }
                //verificar se o identificador existe no estoque
                referenciaProduto = verificarCadastro((String) infosImprimir[0],2,estoque,quantidadeEstoque);
                if (referenciaProduto != -1) {
                    //nome do produto
                    infosImprimir[1] = estoque[referenciaProduto][3];
                    //preço de venda do produto
                    infosImprimir[3] = estoque[referenciaProduto][7];
                    break;
                }
                System.out.println("Identificador invalido!");
            } while (true);

            do {
                try {
                    System.out.println("Quantidade: ");
                    infosImprimir[2] = Integer.parseInt(sc.nextLine());
                    if ((Integer) infosImprimir[2] <= 0) {
                        System.out.println("Valor negativo invalido, digite novamente!");
                    } else if ((Integer) infosImprimir[2] <= (Integer) estoque[referenciaProduto][8]) {
                        //Dando baixa no estoque do produto
                        estoque[referenciaProduto][8] = (Integer) estoque[referenciaProduto][8] - (Integer) infosImprimir[2];
                        break;
                    } else {
                        System.out.println("A quantidade informada é maior que a quantidade disponivel!");
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Quantidade invalida, digite novamente!");
                    sc.next();
                }
            } while (true);

            //Calcular o valor a pagar
            infosImprimir[4] = (Integer) infosImprimir[2] * (Double) infosImprimir[3];
            linha = cadastrar(infosImprimir, produtosVendidos, linha);
        } while (true);
        valorTotalDaVenda(infosVendas, produtosVendidos, linha);
        quantidadeVendas = cadastrar(infosVendas,vendas,quantidadeVendas);
        imprimirResumoDeVendas(produtosVendidos, linha);

    }

    public static void imprimirResumoDeVendas(Object[][] produtosVendidos, int quantidade) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-12s %-15s %-15s %n", "Identificador", "Nome", "Quantidade", "Preco(R$)", "Valor a Pagar(R$)");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (int i = 0; i < quantidade; ++i) {
            System.out.format("%-15s %-20s %-12d %-15.2f %-15.2f %n", produtosVendidos[i][0], produtosVendidos[i][1], (Integer) produtosVendidos[i][2], (Double) produtosVendidos[i][3], (Double) produtosVendidos[i][4]);
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("Valor total da Venda com Desconto de %.0f%%: R$ %.2f %n",(((TipoCliente) vendas[quantidadeVendas-1][1]).getDesconto()*100.d), vendas[quantidadeVendas-1][3]);
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static void valorTotalDaVenda(Object [] infoVendas,Object[][] produtosVendidos, int linha) {
        infoVendas[2] = 0;
        infoVendas[3] = 0.d;
        for (int i = 0; i < linha; i++) {
            //Quantidade total de produtos
            infoVendas[2] = (Integer) infoVendas[2] + (Integer) produtosVendidos[i][2];
            //Preço total de produtos
            infoVendas[3] = (Double) infoVendas[3] + (Double) produtosVendidos[i][4];
        }
        infoVendas[3]= (Double) infoVendas[3] * (1.d - ((TipoCliente) infoVendas[1]).getDesconto());
    }

    public static void imprimirVendasAnalitico(Object[][] produtosVendidos, int quantidade) {
        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-25s %-15s %n", "CPF/CNPJ", "Tipo Cliente", "Quantidade de Produtos", "Valor Pago(R$)");
        System.out.println("----------------------------------------------------------------------------");

        for (int i = 0; i < quantidade; ++i) {
            System.out.format("%-15s %-15s %-25d %-15.2f %n", produtosVendidos[i][0], produtosVendidos[i][1], (Integer) produtosVendidos[i][2], (Double) produtosVendidos[i][3]);
        }
        System.out.println("----------------------------------------------------------------------------");
    }

    public static void calcularVendasSintetico() {
        Object[][] vendasSintetico = new Object[quantidadeVendas][3];
        int quantidadeClientes = 0;
        int linha = 0;
        for (int i = 0; i < quantidadeVendas; i++) {
            //vendasSintetico[i][0] obtem cpf não duplicados
            linha = verificarCadastro((String) vendasSintetico[i][0], 0, vendasSintetico, quantidadeClientes);
            if (linha == -1) {
                vendasSintetico[quantidadeClientes][0] = vendas[i][0];
                vendasSintetico[quantidadeClientes][1] = 0;
                vendasSintetico[quantidadeClientes][2] = 0.d;
                quantidadeClientes++;
            }
        }
        for (int i = 0; i < quantidadeClientes; i++) {
            for (int i1 = 0; i1 < quantidadeVendas; i1++) {
                if (vendasSintetico[i][0].equals(vendas[i1][0])) {
                    vendasSintetico[i][1] = (Integer) vendasSintetico[i][1] + (Integer) vendas[i1][2];
                    vendasSintetico[i][2] = (Double) vendasSintetico[i][2] + (Double) vendas[i1][3];
                }

            }
        }
        imprimirVendasSintetico(vendasSintetico,quantidadeClientes);
    }

    public static void imprimirVendasSintetico(Object[][] produtosVendidos, int quantidade) {
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-15s %-25s %-15s %n", "CPF/CNPJ", "Quantidade de Produtos", "Valor Pago(R$)");
        System.out.println("------------------------------------------------------------------");

        for (int i = 0; i < quantidade; ++i) {
            System.out.format("%-15s %-25d %-15.2f %n", produtosVendidos[i][0], (Integer) produtosVendidos[i][1], (Double) produtosVendidos[i][2]);
        }
        System.out.println("------------------------------------------------------------------");
    }



}
