<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Modules\Comments\Services\CommentService;

class CommentApiController extends Controller
{
    public function __construct(CommentService $service)
    {
        $this->service = $service;
    }

    public function addComment($recipeId, Request $request) {
        return $this->service->addComment($recipeId, $request);
    }
}
