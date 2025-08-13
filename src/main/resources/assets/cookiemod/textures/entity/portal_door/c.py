from PIL import Image
import sys
import os


def vertical_concatenate_images(image_paths, output_path):
    """
    将多张透明背景的PNG图片竖直方向拼接
    
    参数:
        image_paths: 输入图片的路径列表
        output_path: 输出图片的路径
    """
    # 打开所有图片并存储到列表中
    images = []
    max_width = 0
    total_height = 0

    for path in image_paths:
        img = Image.open(path).convert("RGBA")
        images.append(img)
        max_width = max(max_width, img.width)
        total_height += img.height

    # 创建一个新的空白画布，宽度为最大宽度，高度为所有图片高度之和
    result_image = Image.new("RGBA", (max_width, total_height), (255, 255, 255, 0))

    # 将图片依次粘贴到画布上
    y_offset = 0
    for img in images:
        result_image.paste(img, (0, y_offset), img)
        y_offset += img.height

    # 保存结果
    result_image.save(output_path, format='PNG')
    print(f"拼接完成，结果已保存到 {output_path}")


if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("用法: python script.py [输入图片路径1] [输入图片路径2] ... [输出图片路径]")
        print("示例: python script.py image1.png image2.png image3.png output.png")
        sys.exit(1)

    # 获取输入图片路径和输出路径
    input_paths = sys.argv[1:-1]
    output_path = sys.argv[-1]

    # 检查所有输入图片是否存在
    for path in input_paths:
        if not os.path.exists(path):
            print(f"错误: 文件 {path} 不存在")
            sys.exit(1)

    # 执行拼接
    vertical_concatenate_images(input_paths, output_path)
