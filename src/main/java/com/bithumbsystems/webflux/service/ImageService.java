@Service
public class ImageService {
    public static String UPLOAD_PATH = "tmp";

    private final ResourceLoader resourceLoader;
    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    CommandLineRunner setUp() throws IOException {
        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_PATH));
            Files.createDirectory(Paths.get(UPLOAD_PATH));
            FileCopyUtils.copy("file1", new FileWriter(UPLOAD_PATH + "/file1.jpg"));
            FileCopyUtils.copy("file2", new FileWriter(UPLOAD_PATH + "/file2.jpg"));
            FileCopyUtils.copy("file3", new FileWriter(UPLOAD_PATH + "/file3.jpg"));
        };
    }

    public Flux<Image> allImages() {
        try {
            return Flux.fromIterable(
              Files.newDirectoryStream(Paths.get(UPLOAD_PATH))
            ).map(path -> new Image(Integer.toString(path.hashCode()), path.getFileName().toString()));
        }catch(IOException e) {
            return Flux.empty();
        }
    }

    public Mono<Resource> getOneImage(String filename) {
        return Mono.fromSupplier(() ->
                resourceLoader.getResource(
                        "file:" + UPLOAD_PATH + "/"+ filename
                ));
    }

    public Mono<Void> uploadImage(Flux<FilePart> files) {
        return files.fileMap(file -> file.transferTo(
                Paths.get(UPLOAD_PATH, file.filename()
        ).toFile())).then();
    }

    public Mono<Void> deleteImage(String filename) {
        return Mono.fromRunable(() -> {
           try {
               Files.deleteIfExists(Paths.get(UPLOAD_PATH, filename));
           }catch(IOException e) {
               throw new RuntimeExcetpion(e);
           }
        });
    }
}