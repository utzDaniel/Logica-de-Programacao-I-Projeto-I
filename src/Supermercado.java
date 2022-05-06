//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Supermercado {
    private static int quantidadeEstoque = 0;
    private static int quantidadeVendas = 0;
    private static Object[][] estoque = new Object[5][9];
    private static Object[][] vendas = new Object[5][5];

    public Supermercado() {
    }

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        String opcao;
        String opcoes = "0 1 2 3 4 5 6 7 8";
        do {
            while (true) {
                System.out.println("---------------------------------------------------------------------------");
                System.out.println();
                System.out.println("Por favor, selecione o item desejado");
                System.out.println("0 - Saida");
                System.out.println("1 - Cadastrar/Comprar produtos");
                System.out.println("2 - Imprimir estoque");
                System.out.println("3 - Listar os produto pelo Tipo");
                System.out.println("4 - Pesquisar um produto pelo codigo");
                System.out.println("5 - Pesquisar um produto pelo nome usando like");
                System.out.println("6 - Vendas");
                System.out.println("7 - Relatorio de vendas analitico, todas as vendas");
                System.out.println("8 - Relatorios de vendas sintetico, consolidado por CPF");
                System.out.println();
                System.out.println("---------------------------------------------------------------------------");
                opcao = sc.next();

                if (!opcoes.contains(opcao.substring(0,1))) {
                    break;
                }

                sistema(Integer.parseInt(opcao));
            }
        } while (!opcao.equals("0"));

        System.out.println("Saindo do sistema! Tchau, volte sempre!");
    }

    public static void sistema(int opcao) {
        Object[] infos;

        switch (opcao) {
            case 1:
                infos = inserirInfos();
                int status = verificarCadastro(infos);
                if (status == -1) {
                    cadastrar(infos,estoque,quantidadeEstoque);
                    estoque[quantidadeEstoque-1][8] = 0;
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

                break;
            case 7:
                // relatorio analitico

                break;
            case 8:
                // relatorio com o cpf igual

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

    public static int verificarCadastro(Object[] infos) {
        for (int i = 0; i < quantidadeEstoque; ++i) {
            if (estoque[i][1].equals(infos[1]) && estoque[i][2].equals(infos[2])) {
                return i;
            }
        }
        return -1;
    }

    public static void cadastrar(Object[] infos, Object[][] matriz, int quantidade) {
        if (quantidade == matriz.length) {
            redimensionarEficiente();
        }

        for (int i = 0; i < infos.length; ++i) {
            matriz[quantidade][i] = infos[i];
        }

        // condicao de estoque sempre zero
        ++quantidade;
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
        System.out.printf("%-10s %-20s %-15s %-20s %-15s %-12s %-20s %-15s %-10s", "Tipo", "Marca", "Identificador", "Nome", "Preco Custo", "Quantidade", "Data Compra", "Preco de venda", "Estoque");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < quantidade; ++i) {
            System.out.format("%-10s %-20s %-15s %-20s %-15.2f %-12d %-20s %-15.2f %-10d", estoque[i][0], estoque[i][1], estoque[i][2], estoque[i][3], (Double) estoque[i][4], (Integer) estoque[i][5], estoque[i][6], (Double) estoque[i][7], (Integer) estoque[i][8]);
            System.out.println();
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
            if(estoque[i][0].equals(Tipo.values()[Integer.parseInt(opcao)-1])){
                for (int i1 = 0; i1 < estoque[i].length; i1++) {
                    lista[contador][i1] = estoque[i][i1];
                }
                contador++;
            }
        }
        imprimirTabela(lista,contador);
    }

    public static void pesquisarPorCodigo(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor, insira um identificador.");
        String identificador = sc.nextLine();

        Object[][] produto = new Object[1][estoque[0].length];
        for (int i = 0; i < quantidadeEstoque; i++) {
            if (estoque[i][2].equals(identificador)){

                for (int i1 = 0; i1 < estoque[i].length; i1++) {
                    produto[0][i1] = estoque[i][i1];
                }
                imprimirTabela(produto,1);
                return;
            }
        }
        System.out.println("Identificador nao encontrado!");
    }

    public static void pesquisarPorNome(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor, insira um Nome.");
        String nome = sc.nextLine();

        Object[][] produto = new Object[1][estoque[0].length];
        for (int i = 0; i < quantidadeEstoque; i++) {
            if (((String) estoque[i][3]).contains(nome)){

                for (int i1 = 0; i1 < estoque[i].length; i1++) {
                    produto[0][i1] = estoque[i][i1];
                }
                imprimirTabela(produto,1);
                return;
            }
        }
        System.out.println("Nome nao encontrado!");
    }

    public static void venda(){
        Scanner sc = new Scanner(System.in);
        String cpf;

        do {
            System.out.println("Deseja inserir o CPF?");
            System.out.println("S - Sim");
            System.out.println("N - Nao");
            String opcao = sc.nextLine();

            // verificar se opcao esta ok
            if (opcao.contains("S")) {
                do {
                    System.out.println("Digite o CPF: ");
                    cpf = sc.nextLine();
                    if (cpf.length() != 11){
                        System.out.println("Erro na quantidadeEstoque de numeros, digite novamente!");
                    }
                }while(cpf.length() != 11);

                tipoDeCliente();

                break;
            } else if (opcao.contains("N")) {
                cpf = "00000000191";
                break;
            } else {
                System.out.println("Opcao invalida, digite novamente!");
            }
        } while(true);

    }

    public static String tipoDeCliente(){
        Scanner sc = new Scanner(System.in);
        String tipoCliente;
        do {
            System.out.println("Que tipo de cliente?");
            for (int i = 0; i < TipoCliente.values().length; i++) {
                System.out.printf("%d - %s %n", i + 1, TipoCliente.values()[i]);
            }
            tipoCliente = sc.next();
            if (tipoCliente.equals("1") || tipoCliente.equals("2") || tipoCliente.equals("3")) {
                return tipoCliente;
            }
        } while (true);

    }


}
