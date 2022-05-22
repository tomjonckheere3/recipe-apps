<?php
namespace App\Modules\Comments\Services;

use App\Models\Comment;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class CommentService {

    protected $rules = [
        "userId" => "required|integer",
        "commentContent" => "required|string|min:1"
    ];

    public function __construct(Comment $commentModel)
    {
        $this->commentModel = $commentModel;
    }

    public function addComment($recipeId, Request $request) {
        $this->errors = null;
        $validator = Validator::make($request->all(), $this->rules);

        if($validator->fails()) {
            $this->errors = $validator->errors();
            Log::info($request->getContent());
            Log::info($this->errors);
            return response()->json($validator->errors()->toJson(), 400);
        }

        $request["recipeId"] = $recipeId;

        $this->commentModel->create($request->all());

        return response()->json("Added comment", 201);
    }
}
