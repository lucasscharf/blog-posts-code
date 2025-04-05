import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class TesteStream {

  public static void main(String[] args) {
    List<String> usuarios = List.of("Alice");

    List<String> produtos = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
          produtos.add("usuario-" + i);
        }
        // ImmutableCollections.List
        // ReferencePipeline
        // AbstractPipeline
        // ImmutableCollections
        //ForkJoinPool
    Stream<String> streamExterno = usuarios.stream()
        .flatMap(usuario -> {
          Stream<String> streamInterno = produtos.parallelStream()
              .map(produto -> {
                // System.out.println("Map sequencial: " + usuario + " - " + produto + " na " +
                // Thread.currentThread().getName());
                return usuario + " → " + produto;
              });
          boolean parallel = streamInterno.isParallel();
          System.out.println("StreamInterno: " + usuario + " Thread: " +
              Thread.currentThread().getName() + " IsParallel: " + parallel);
          return streamInterno;
        });

    boolean isExternoParalelo = streamExterno.isParallel();
    streamExterno.parallel()
    streamExterno.forEach(resultado -> {
      // System.out.println("Resultado: " + resultado + " na " + Thread.currentThread().getName());
    });
    List<String> resultado = streamExterno.toList();
    System.out
        .println("StreamExterno: " + Thread.currentThread().getName() + " Is Parallel: " + isExternoParalelo);
  }
}