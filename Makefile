# ソースファイルとディレクトリ
SRC_DIR := c
BIN_DIR := bin/c

SRCS := $(wildcard $(SRC_DIR)/*.c)
OBJS := $(patsubst $(SRC_DIR)/%.c, $(BIN_DIR)/%.o, $(SRCS))

# 出力バイナリ
TARGET := $(BIN_DIR)/main

# コンパイラ
CC := gcc
CFLAGS := -Wall -Wextra -std=c11

# デフォルトルール
all: $(TARGET)

# 実行バイナリをリンク
$(TARGET): $(OBJS)
	@mkdir -p $(BIN_DIR)
	$(CC) $(OBJS) -o $@

# .o ファイル生成ルール
$(BIN_DIR)/%.o: $(SRC_DIR)/%.c
	@mkdir -p $(BIN_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

# 実行ルール
run: $(TARGET)
	./$(TARGET)

# クリーンルール
clean:
	rm -rf $(BIN_DIR)
